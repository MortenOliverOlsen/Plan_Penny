/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package morten.plan_penny.Tasks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import morten.plan_penny.R;

/**
 * The dynamic listview is an extension of listview that supports cell dragging
 * and swapping.
 *
 * This layout is in charge of positioning the hover cell in the correct location
 * on the screen in response to user touch events. It uses the position of the
 * hover cell to determine when two cells should be swapped. If two cells should
 * be swapped, all the corresponding data set and layout changes are handled here.
 *
 * If no cell is selected, all the touch events are passed down to the listview
 * and behave normally. If one of the items in the listview experiences a
 * long press event, the contents of its current visible state are captured as
 * a bitmap and its visibility is set to INVISIBLE. A hover cell is then created and
 * added to this layout as an overlaying BitmapDrawable above the listview. Once the
 * hover cell is translated some distance to signify an item swap, a data set change
 * accompanied by animation takes place. When the user releases the hover cell,
 * it animates into its corresponding position in the listview.
 *
 * When the hover cell is either above or below the bounds of the listview, this
 * listview also scrolls on its own so as to reveal additional content.
 */
public class DynamicListView extends ListView {

    private final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 15;
    private final int MOVE_DURATION = 150;
    private final int LINE_THICKNESS = 15;

    public ArrayList<TaskListItem> taskList;

    private int mLastEventY = -1;

    private int mDownY = -1;
    private int mDownX = -1;

    private int mTotalOffset = 0;

    private boolean mCellIsMobile = false;
    private boolean mIsMobileScrolling = false;
    private int mSmoothScrollAmountAtEdge = 0;

    private final int INVALID_ID = -1;
    private long mAboveItemId = INVALID_ID;
    private long mMobileItemId = INVALID_ID;
    private long mBelowItemId = INVALID_ID;

    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;

    private final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    private boolean mIsWaitingForScrollFinish = false;
    private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;

    public DynamicListView(Context context) {
        super(context);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // Expanding list attributes

    private boolean mShouldRemoveObserver = false;

    private List<View> mViewsToDraw = new ArrayList<>();

    private int[] mTranslate;


    public void init(Context context) {
        setOnItemLongClickListener(mOnItemLongClickListener);
        setOnItemClickListener(mItemClickListener);
        setOnScrollListener(mScrollListener);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mSmoothScrollAmountAtEdge = (int)(SMOOTH_SCROLL_AMOUNT_AT_EDGE / metrics.density);
    }

    /**
     * Listens for long clicks on any items in the listview. When a cell has
     * been selected, the hover cell is created and set up.
     */
    private OnItemLongClickListener mOnItemLongClickListener =
            new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    mTotalOffset = 0;

                    int position = pointToPosition(mDownX, mDownY);
                    int itemNum = position - getFirstVisiblePosition();

                    View selectedView = getChildAt(itemNum);
                    mMobileItemId = getAdapter().getItemId(position);
                    mHoverCell = getAndAddHoverView(selectedView);
                    selectedView.setVisibility(INVISIBLE);

                    mCellIsMobile = true;

                    updateNeighborViewsForID(mMobileItemId);

                    return true;
                }
            };

    /**
     * Creates the hover cell with the appropriate bitmap and of appropriate
     * size. The hover cell's BitmapDrawable is drawn on top of the bitmap every
     * single time an invalidate call is made.
     */
    private BitmapDrawable getAndAddHoverView(View v) {

        int w = v.getWidth();
        int h = v.getHeight();
        int top = v.getTop();
        int left = v.getLeft();

        Bitmap b = getBitmapFromView(v);

        BitmapDrawable drawable = new BitmapDrawable(getResources(), b);

        mHoverCellOriginalBounds = new Rect(left, top, left + w, top + h);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);

        drawable.setBounds(mHoverCellCurrentBounds);

        return drawable;
    }

    /** Draws a black border over the screenshot of the view passed in. */
    private Bitmap getBitmapWithBorder(View v) {
        Bitmap bitmap = getBitmapFromView(v);
        Canvas can = new Canvas(bitmap);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(LINE_THICKNESS);
        paint.setColor(Color.BLACK);

        can.drawBitmap(bitmap, 0, 0, null);
        can.drawRect(rect, paint);

        return bitmap;
    }

    /** Returns a bitmap showing a screenshot of the view passed in. */
    private Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * Stores a reference to the views above and below the item currently
     * corresponding to the hover cell. It is important to note that if this
     * item is either at the top or bottom of the list, mAboveItemId or mBelowItemId
     * may be invalid.
     */
    private void updateNeighborViewsForID(long itemID) {
        int position = getPositionForID(itemID);
        StableArrayAdapter adapter = ((StableArrayAdapter)getAdapter());
        mAboveItemId = adapter.getItemId(position - 1);
        mBelowItemId = adapter.getItemId(position + 1);
    }

    /** Retrieves the view in the list corresponding to itemID */
    public View getViewForID (long itemID) {
        int firstVisiblePosition = getFirstVisiblePosition();
        StableArrayAdapter adapter = ((StableArrayAdapter)getAdapter());
        for(int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int position = firstVisiblePosition + i;
            long id = adapter.getItemId(position);
            if (id == itemID) {
                return v;
            }
        }
        return null;
    }

    /** Retrieves the position in the list corresponding to itemID */
    public int getPositionForID (long itemID) {
        View v = getViewForID(itemID);
        if (v == null) {
            return -1;
        } else {
            return getPositionForView(v);
        }
    }

    /**
     *  dispatchDraw gets invoked when all the child views are about to be drawn.
     *  By overriding this method, the hover cell (BitmapDrawable) can be drawn
     *  over the listview's items whenever the listview is redrawn.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHoverCell != null) {
            mHoverCell.draw(canvas);
        }
        if (mViewsToDraw.size() == 0) {
            return;
        }

        for (View v: mViewsToDraw) {
            canvas.translate(0, v.getTop());
            v.draw(canvas);
            canvas.translate(0, -v.getTop());
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int)event.getX();
                mDownY = (int)event.getY();
                mActivePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER_ID) {
                    break;
                }

                int pointerIndex = event.findPointerIndex(mActivePointerId);

                mLastEventY = (int) event.getY(pointerIndex);
                int deltaY = mLastEventY - mDownY;

                if (mCellIsMobile) {
                    mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left,
                            mHoverCellOriginalBounds.top + deltaY + mTotalOffset);
                    mHoverCell.setBounds(mHoverCellCurrentBounds);
                    invalidate();

                    handleCellSwitch();

                    mIsMobileScrolling = false;
                    handleMobileCellScroll();

                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchEventsEnded();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                /* If a multitouch event took place and the original touch dictating
                 * the movement of the hover cell has ended, then the dragging event
                 * ends and the hover cell is animated to its corresponding position
                 * in the listview. */
                pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                        MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    touchEventsEnded();
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * This method determines whether the hover cell has been shifted far enough
     * to invoke a cell swap. If so, then the respective cell swap candidate is
     * determined and the data set is changed. Upon posting a notification of the
     * data set change, a layout is invoked to place the cells in the right place.
     * Using a ViewTreeObserver and a corresponding OnPreDrawListener, we can
     * offset the cell being swapped to where it previously was and then animate it to
     * its new position.
     */
    private void handleCellSwitch() {
        final int deltaY = mLastEventY - mDownY;
        int deltaYTotal = mHoverCellOriginalBounds.top + mTotalOffset + deltaY;

        View belowView = getViewForID(mBelowItemId);
        View mobileView = getViewForID(mMobileItemId);
        View aboveView = getViewForID(mAboveItemId);

        boolean isBelow = (belowView != null) && (deltaYTotal > belowView.getTop());
        boolean isAbove = (aboveView != null) && (deltaYTotal < aboveView.getTop());

        if (isBelow || isAbove) {

            final long switchItemID = isBelow ? mBelowItemId : mAboveItemId;
            View switchView = isBelow ? belowView : aboveView;
            final int originalItem = getPositionForView(mobileView);

            if (switchView == null) {
                updateNeighborViewsForID(mMobileItemId);
                return;
            }

            swapElements(taskList, originalItem, getPositionForView(switchView));

            ((BaseAdapter) getAdapter()).notifyDataSetChanged();

            mDownY = mLastEventY;

            final int switchViewStartTop = switchView.getTop();

            mobileView.setVisibility(View.VISIBLE);
            switchView.setVisibility(View.INVISIBLE);

            updateNeighborViewsForID(mMobileItemId);

            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    observer.removeOnPreDrawListener(this);

                    View switchView = getViewForID(switchItemID);

                    mTotalOffset += deltaY;

                    int switchViewNewTop = switchView.getTop();
                    int delta = switchViewStartTop - switchViewNewTop;

                    switchView.setTranslationY(delta);

                    ObjectAnimator animator = ObjectAnimator.ofFloat(switchView,
                            View.TRANSLATION_Y, 0);
                    animator.setDuration(MOVE_DURATION);
                    animator.start();

                    return true;
                }
            });
        }
    }

    private void swapElements(ArrayList arrayList, int indexOne, int indexTwo) {
        Object temp = arrayList.get(indexOne);
        arrayList.set(indexOne, arrayList.get(indexTwo));
        arrayList.set(indexTwo, temp);
    }


    /**
     * Resets all the appropriate fields to a default state while also animating
     * the hover cell back to its correct location.
     */
    private void touchEventsEnded () {
        final View mobileView = getViewForID(mMobileItemId);
        if (mCellIsMobile|| mIsWaitingForScrollFinish) {
            mCellIsMobile = false;
            mIsWaitingForScrollFinish = false;
            mIsMobileScrolling = false;
            mActivePointerId = INVALID_POINTER_ID;

            // If the autoscroller has not completed scrolling, we need to wait for it to
            // finish in order to determine the final location of where the hover cell
            // should be animated to.
            if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) {
                mIsWaitingForScrollFinish = true;
                return;
            }

            mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, mobileView.getTop());

            ObjectAnimator hoverViewAnimator = ObjectAnimator.ofObject(mHoverCell, "bounds",
                    sBoundEvaluator, mHoverCellCurrentBounds);
            hoverViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    invalidate();
                }
            });
            hoverViewAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAboveItemId = INVALID_ID;
                    mMobileItemId = INVALID_ID;
                    mBelowItemId = INVALID_ID;
                    mobileView.setVisibility(VISIBLE);
                    mHoverCell = null;
                    setEnabled(true);
                    invalidate();
                }
            });
            hoverViewAnimator.start();
        } else {
            touchEventsCancelled();
        }
    }

    /**
     * Resets all the appropriate fields to a default state.
     */
    private void touchEventsCancelled () {
        View mobileView = getViewForID(mMobileItemId);
        if (mCellIsMobile) {
            mAboveItemId = INVALID_ID;
            mMobileItemId = INVALID_ID;
            mBelowItemId = INVALID_ID;
            mobileView.setVisibility(VISIBLE);
            mHoverCell = null;
            invalidate();
        }
        mCellIsMobile = false;
        mIsMobileScrolling = false;
        mActivePointerId = INVALID_POINTER_ID;
    }

    /**
     * This TypeEvaluator is used to animate the BitmapDrawable back to its
     * final location when the user lifts his finger by modifying the
     * BitmapDrawable's bounds.
     */
    private final static TypeEvaluator<Rect> sBoundEvaluator = new TypeEvaluator<Rect>() {
        public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
            return new Rect(interpolate(startValue.left, endValue.left, fraction),
                    interpolate(startValue.top, endValue.top, fraction),
                    interpolate(startValue.right, endValue.right, fraction),
                    interpolate(startValue.bottom, endValue.bottom, fraction));
        }

        public int interpolate(int start, int end, float fraction) {
            return (int)(start + fraction * (end - start));
        }
    };

    /**
     *  Determines whether this listview is in a scrolling state invoked
     *  by the fact that the hover cell is out of the bounds of the listview;
     */
    private void handleMobileCellScroll() {
        mIsMobileScrolling = handleMobileCellScroll(mHoverCellCurrentBounds);
    }

    /**
     * This method is in charge of determining if the hover cell is above
     * or below the bounds of the listview. If so, the listview does an appropriate
     * upward or downward smooth scroll so as to reveal new items.
     */
    public boolean handleMobileCellScroll(Rect r) {
        int offset = computeVerticalScrollOffset();
        int height = getHeight();
        int extent = computeVerticalScrollExtent();
        int range = computeVerticalScrollRange();
        int hoverViewTop = r.top;
        int hoverHeight = r.height();

        if (hoverViewTop <= 0 && offset > 0) {
            smoothScrollBy(-mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        if (hoverViewTop + hoverHeight >= height && (offset + extent) < range) {
            smoothScrollBy(mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        return false;
    }

    public void setTaskList(ArrayList<TaskListItem> taskListData) {
        taskList = taskListData;
    }

    /**
     * This scroll listener is added to the listview in order to handle cell swapping
     * when the cell is either at the top or bottom edge of the listview. If the hover
     * cell is at either edge of the listview, the listview will begin scrolling. As
     * scrolling takes place, the listview continuously checks if new cells became visible
     * and determines whether they are potential candidates for a cell swap.
     */
    private OnScrollListener mScrollListener = new OnScrollListener () {

        private int mPreviousFirstVisibleItem = -1;
        private int mPreviousVisibleItemCount = -1;
        private int mCurrentFirstVisibleItem;
        private int mCurrentVisibleItemCount;
        private int mCurrentScrollState;

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            mCurrentFirstVisibleItem = firstVisibleItem;
            mCurrentVisibleItemCount = visibleItemCount;

            mPreviousFirstVisibleItem = (mPreviousFirstVisibleItem == -1) ? mCurrentFirstVisibleItem
                    : mPreviousFirstVisibleItem;
            mPreviousVisibleItemCount = (mPreviousVisibleItemCount == -1) ? mCurrentVisibleItemCount
                    : mPreviousVisibleItemCount;

            checkAndHandleFirstVisibleCellChange();
            checkAndHandleLastVisibleCellChange();

            mPreviousFirstVisibleItem = mCurrentFirstVisibleItem;
            mPreviousVisibleItemCount = mCurrentVisibleItemCount;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mCurrentScrollState = scrollState;
            mScrollState = scrollState;
            isScrollCompleted();
        }

        /**
         * This method is in charge of invoking 1 of 2 actions. Firstly, if the listview
         * is in a state of scrolling invoked by the hover cell being outside the bounds
         * of the listview, then this scrolling event is continued. Secondly, if the hover
         * cell has already been released, this invokes the animation for the hover cell
         * to return to its correct position after the listview has entered an idle scroll
         * state.
         */
        private void isScrollCompleted() {
            if (mCurrentVisibleItemCount > 0 && mCurrentScrollState == SCROLL_STATE_IDLE) {
                if (mCellIsMobile && mIsMobileScrolling) {
                    handleMobileCellScroll();
                } else if (mIsWaitingForScrollFinish) {
                    touchEventsEnded();
                }
            }
        }

        /**
         * Determines if the listview scrolled up enough to reveal a new cell at the
         * top of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleFirstVisibleCellChange() {
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID) {
                    updateNeighborViewsForID(mMobileItemId);
                    handleCellSwitch();
                }
            }
        }

        /**
         * Determines if the listview scrolled down enough to reveal a new cell at the
         * bottom of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleLastVisibleCellChange() {
            int currentLastVisibleItem = mCurrentFirstVisibleItem + mCurrentVisibleItemCount;
            int previousLastVisibleItem = mPreviousFirstVisibleItem + mPreviousVisibleItemCount;
            if (currentLastVisibleItem != previousLastVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID) {
                    updateNeighborViewsForID(mMobileItemId);
                    handleCellSwitch();
                }
            }
        }
    };

    public void addRow(TaskListItem newObj){
    final StableArrayAdapter adapter = (StableArrayAdapter)getAdapter();
    taskList.add(0, newObj);
    adapter.addStableIdForDataAtPosition(0);
    adapter.notifyDataSetChanged();
    }

    // Added methods for expanding cells

    /**
     * This method takes some view and the values by which its top and bottom bounds
     * should be changed by. Given these params, an animation which will animate
     * these bound changes is created and returned.
     */

    private Animator getAnimation(final View view, float translateTop, float translateBottom) {

        int top = view.getTop();
        int bottom = view.getBottom();

        int endTop = (int)(top + translateTop);
        int endBottom = (int)(bottom + translateBottom);

        PropertyValuesHolder translationTop = PropertyValuesHolder.ofInt("top", top, endTop);
        PropertyValuesHolder translationBottom = PropertyValuesHolder.ofInt("bottom", bottom,
                endBottom);

        return ObjectAnimator.ofPropertyValuesHolder(view, translationTop, translationBottom);
    }

    /**
     * This method collapses the view that was clicked and animates all the views
     * around it to close around the collapsing view. There are several steps required
     * to do this which are outlined below.
     *
     * 1. Update the layout parameters of the view clicked so as to minimize its height
     *    to the original collapsed (default) state.
     * 2. After invoking a layout, the listview will shift all the cells so as to display
     *    them most efficiently. Therefore, during the first predraw pass, the listview
     *    must be offset by some amount such that given the custom bound change upon
     *    collapse, all the cells that need to be on the screen after the layout
     *    are rendered by the listview.
     * 3. On the second predraw pass, all the items are first returned to their original
     *    location (before the first layout).
     * 4. The collapsing view's bounds are animated to what the final values should be.
     * 5. The bounds above the collapsing view are animated downwards while the bounds
     *    below the collapsing view are animated upwards.
     * 6. The extra text is faded out as its contents become visible throughout the
     *    animation process.
     */

    private void collapseView(final View view) {
        final TaskListItem viewObject = (TaskListItem)getItemAtPosition
                (getPositionForView(view));

        /* Store the original top and bottom bounds of all the cells.*/
        final int oldTop = view.getTop();
        final int oldBottom = view.getBottom();

        final HashMap<View, int[]> oldCoordinates = new HashMap<View, int[]>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
          //  v.setHasTransientState(true);
            oldCoordinates.put(v, new int [] {v.getTop(), v.getBottom()});
        }

        /* Update the layout so the extra content becomes invisible.*/
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                viewObject.getCollapsedHeight()));

         /* Add an onPreDraw listener. */
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                if (!mShouldRemoveObserver) {
                    /*Same as for expandingView, the parameters for setSelectionFromTop must
                    * be determined such that the necessary cells of the ListView are rendered
                    * and added to it.*/
                    mShouldRemoveObserver = true;

                    int newTop = view.getTop();
                    int newBottom = view.getBottom();

                    int newHeight = newBottom - newTop;
                    int oldHeight = oldBottom - oldTop;
                    int deltaHeight = oldHeight - newHeight;

                    mTranslate = getTopAndBottomTranslations(oldTop, oldBottom, deltaHeight, false);

                    int currentTop = view.getTop();
                    int futureTop = oldTop + mTranslate[0];

                    int firstChildStartTop = getChildAt(0).getTop();
                    int firstVisiblePosition = getFirstVisiblePosition();
                    int deltaTop = currentTop - futureTop;

                    int i;
                    int childCount = getChildCount();
                    for (i = 0; i < childCount; i++) {
                        View v = getChildAt(i);
                        int height = v.getBottom() - Math.max(0, v.getTop());
                        if (deltaTop - height > 0) {
                            firstVisiblePosition++;
                            deltaTop -= height;
                        } else {
                            break;
                        }
                    }

                    if (i > 0) {
                        firstChildStartTop = 0;
                    }

                    setSelectionFromTop(firstVisiblePosition, firstChildStartTop - deltaTop);

                    requestLayout();

                    return false;
                }

                mShouldRemoveObserver = false;
                observer.removeOnPreDrawListener(this);

                int yTranslateTop = mTranslate[0];
                int yTranslateBottom = mTranslate[1];

                int index = indexOfChild(view);
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    int [] old = oldCoordinates.get(v);
                    if (old != null) {
                        /* If the cell was present in the ListView before the collapse and
                        * after the collapse then the bounds are reset to their old values.*/
                        v.setTop(old[0]);
                        v.setBottom(old[1]);
                    //    v.setHasTransientState(false);
                    } else {
                        /* If the cell is present in the ListView after the collapse but
                         * not before the collapse then the bounds are calculated using
                         * the bottom and top translation of the collapsing cell.*/
                        int delta = i > index ? yTranslateBottom : -yTranslateTop;
                        v.setTop(v.getTop() + delta);
                        v.setBottom(v.getBottom() + delta);
                    }
                }

                final View expandingLayout = view.findViewById (R.id.expanding_layout);

                /* Animates all the cells present on the screen after the collapse. */
                ArrayList <Animator> animations = new ArrayList<Animator>();
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    if (v != view) {
                        float diff = i > index ? -yTranslateBottom : yTranslateTop;
                        animations.add(getAnimation(v, diff, diff));
                    }
                }


                /* Adds animation for collapsing the cell that was clicked. */
                animations.add(getAnimation(view, yTranslateTop, -yTranslateBottom));

                /* Adds an animation for fading out the extra content. */
                animations.add(ObjectAnimator.ofFloat(expandingLayout, View.ALPHA, 1, 0));

                /* Disabled the ListView for the duration of the animation.*/
                setEnabled(false);
                setClickable(false);

                /* Play all the animations created above together at the same time. */
                AnimatorSet s = new AnimatorSet();
                s.playTogether(animations);
                s.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        expandingLayout.setVisibility(View.GONE);
                        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView
                                .LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                        viewObject.setExpanded(false);
                        setEnabled(true);
                        setClickable(true);
                        /* Note that alpha must be set back to 1 in case this view is reused
                        * by a cell that was expanded, but not yet collapsed, so its state
                        * should persist in an expanded state with the extra content visible.*/
                        expandingLayout.setAlpha(1);
                    }
                });
                s.start();

                return true;
            }
        });
    }


    /**
     * This method expands the view that was clicked and animates all the views
     * around it to make room for the expanding view. There are several steps required
     * to do this which are outlined below.
     *
     * 1. Store the current top and bottom bounds of each visible item in the listview.
     * 2. Update the layout parameters of the selected view. In the context of this
     *    method, the view should be originally collapsed and set to some custom height.
     *    The layout parameters are updated so as to wrap the content of the additional
     *    text that is to be displayed.
     *
     * After invoking a layout to take place, the listview will order all the items
     * such that there is space for each view. This layout will be independent of what
     * the bounds of the items were prior to the layout so two pre-draw passes will
     * be made. This is necessary because after the layout takes place, some views that
     * were visible before the layout may now be off bounds but a reference to these
     * views is required so the animation completes as intended.
     *
     * 3. The first predraw pass will set the bounds of all the visible items to
     *    their original location before the layout took place and then force another
     *    layout. Since the bounds of the cells cannot be set directly, the method
     *    setSelectionFromTop can be used to achieve a very similar effect.
     * 4. The expanding view's bounds are animated to what the final values should be
     *    from the original bounds.
     * 5. The bounds above the expanding view are animated upwards while the bounds
     *    below the expanding view are animated downwards.
     * 6. The extra text is faded in as its contents become visible throughout the
     *    animation process.
     *
     * It is important to note that the listview is disabled during the animation
     * because the scrolling behaviour is unpredictable if the bounds of the items
     * within the listview are not constant during the scroll.
     */

    private void expandView(final View view) {
        final TaskListItem viewObject = (TaskListItem)getItemAtPosition(getPositionForView
                (view));

        /* Store the original top and bottom bounds of all the cells.*/
        final int oldTop = view.getTop();
        final int oldBottom = view.getBottom();

        final HashMap<View, int[]> oldCoordinates = new HashMap<View, int[]>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
          //  v.setHasTransientState(true);
            oldCoordinates.put(v, new int[] {v.getTop(), v.getBottom()});
        }

        /* Update the layout so the extra content becomes visible.*/
        final View expandingLayout = view.findViewById(R.id.expanding_layout);
        expandingLayout.setVisibility(View.VISIBLE);

        /* Add an onPreDraw Listener to the listview. onPreDraw will get invoked after onLayout
        * and onMeasure have run but before anything has been drawn. This
        * means that the final post layout properties for all the items have already been
        * determined, but still have not been rendered onto the screen.*/
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                /* Determine if this is the first or second pass.*/
                if (!mShouldRemoveObserver) {
                    mShouldRemoveObserver = true;

                    /* Calculate what the parameters should be for setSelectionFromTop.
                    * The ListView must be offset in a way, such that after the animation
                    * takes place, all the cells that remain visible are rendered completely
                    * by the ListView.*/
                    int newTop = view.getTop();
                    int newBottom = view.getBottom();

                    int newHeight = newBottom - newTop;
                    int oldHeight = oldBottom - oldTop;
                    int delta = newHeight - oldHeight;

                    mTranslate = getTopAndBottomTranslations(oldTop, oldBottom, delta, true);

                    int currentTop = view.getTop();
                    int futureTop = oldTop - mTranslate[0];

                    int firstChildStartTop = getChildAt(0).getTop();
                    int firstVisiblePosition = getFirstVisiblePosition();
                    int deltaTop = currentTop - futureTop;

                    int i;
                    int childCount = getChildCount();
                    for (i = 0; i < childCount; i++) {
                        View v = getChildAt(i);
                        int height = v.getBottom() - Math.max(0, v.getTop());
                        if (deltaTop - height > 0) {
                            firstVisiblePosition++;
                            deltaTop -= height;
                        } else {
                            break;
                        }
                    }

                    if (i > 0) {
                        firstChildStartTop = 0;
                    }

                    setSelectionFromTop(firstVisiblePosition, firstChildStartTop - deltaTop);

                    /* Request another layout to update the layout parameters of the cells.*/
                    requestLayout();

                    /* Return false such that the ListView does not redraw its contents on
                     * this layout but only updates all the parameters associated with its
                     * children.*/
                    return false;
                }

                /* Remove the predraw listener so this method does not keep getting called. */
                mShouldRemoveObserver = false;
                observer.removeOnPreDrawListener(this);

                int yTranslateTop = mTranslate[0];
                int yTranslateBottom = mTranslate[1];

                ArrayList <Animator> animations = new ArrayList<Animator>();

                int index = indexOfChild(view);

                /* Loop through all the views that were on the screen before the cell was
                *  expanded. Some cells will still be children of the ListView while
                *  others will not. The cells that remain children of the ListView
                *  simply have their bounds animated appropriately. The cells that are no
                *  longer children of the ListView also have their bounds animated, but
                *  must also be added to a list of views which will be drawn in dispatchDraw.*/
                for (View v: oldCoordinates.keySet()) {
                    int[] old = oldCoordinates.get(v);
                    v.setTop(old[0]);
                    v.setBottom(old[1]);
                    if (v.getParent() == null) {
                        mViewsToDraw.add(v);
                        int delta = old[0] < oldTop ? -yTranslateTop : yTranslateBottom;
                        animations.add(getAnimation(v, delta, delta));
                    } else {
                        int i = indexOfChild(v);
                        if (v != view) {
                            int delta = i > index ? yTranslateBottom : -yTranslateTop;
                            animations.add(getAnimation(v, delta, delta));
                        }
                      //  v.setHasTransientState(false);
                    }
                }

                /* Adds animation for expanding the cell that was clicked. */
                animations.add(getAnimation(view, -yTranslateTop, yTranslateBottom));

                /* Adds an animation for fading in the extra content. */
                animations.add(ObjectAnimator.ofFloat(view.findViewById(R.id.expanding_layout),
                        View.ALPHA, 0, 1));

                /* Disabled the ListView for the duration of the animation.*/
                setEnabled(false);
                setClickable(false);

                /* Play all the animations created above together at the same time. */
                AnimatorSet s = new AnimatorSet();
                s.playTogether(animations);
                s.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewObject.setExpanded(true);
                        setEnabled(true);
                        setClickable(true);
                        if (mViewsToDraw.size() > 0) {
                            for (View v : mViewsToDraw) {
                            //    v.setHasTransientState(false);
                            }
                        }
                        mViewsToDraw.clear();
                    }
                });
                s.start();
                return true;
            }
        });
    }


    /**
     * Calculates the top and bottom bound changes of the selected item. These values are
     * also used to move the bounds of the items around the one that is actually being
     * expanded or collapsed.
     *
     * This method can be modified to achieve different user experiences depending
     * on how you want the cells to expand or collapse. In this specific demo, the cells
     * always try to expand downwards (leaving top bound untouched), and similarly,
     * collapse upwards (leaving top bound untouched). If the change in bounds
     * results in the complete disappearance of a cell, its lower bound is moved is
     * moved to the top of the screen so as not to hide any additional content that
     * the user has not interacted with yet. Furthermore, if the collapsed cell is
     * partially off screen when it is first clicked, it is translated such that its
     * full contents are visible. Lastly, this behaviour varies slightly near the bottom
     * of the listview in order to account for the fact that the bottom bounds of the actual
     * listview cannot be modified.
     */
    private int[] getTopAndBottomTranslations(int top, int bottom, int yDelta,
                                              boolean isExpanding) {
        int yTranslateTop = 0;
        int yTranslateBottom = yDelta;

        int height = bottom - top;

        if (isExpanding) {
            boolean isOverTop = top < 0;
            boolean isBelowBottom = (top + height + yDelta) > getHeight();
            if (isOverTop) {
                yTranslateTop = top;
                yTranslateBottom = yDelta - yTranslateTop;
            } else if (isBelowBottom){
                int deltaBelow = top + height + yDelta - getHeight();
                yTranslateTop = top - deltaBelow < 0 ? top : deltaBelow;
                yTranslateBottom = yDelta - yTranslateTop;
            }
        } else {
            int offset = computeVerticalScrollOffset();
            int range = computeVerticalScrollRange();
            int extent = computeVerticalScrollExtent();
            int leftoverExtent = range-offset - extent;

            boolean isCollapsingBelowBottom = (yTranslateBottom > leftoverExtent);
            boolean isCellCompletelyDisappearing = bottom - yTranslateBottom < 0;

            if (isCollapsingBelowBottom) {
                yTranslateTop = yTranslateBottom - leftoverExtent;
                yTranslateBottom = yDelta - yTranslateTop;
            } else if (isCellCompletelyDisappearing) {
                yTranslateBottom = bottom;
                yTranslateTop = yDelta - yTranslateBottom;
            }
        }

        return new int[] {yTranslateTop, yTranslateBottom};
    }

    /**
     * Listens for item clicks and expands or collapses the selected view depending on
     * its current state.
     */
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView
            .OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
            TaskListItem viewObject = (TaskListItem)getItemAtPosition(getPositionForView
                    (view));
            if (!viewObject.isExpanded()) {
                expandView(view);
            } else {
                collapseView(view);
            }
        }
    };


}