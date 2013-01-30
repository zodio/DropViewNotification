/**
 * Copyright 2013 Poohdish Rattanavijai

 This software is licensed under the GNU General Public License version 2 (the "GPL License").
 You may choose either license to govern your use of this software only upon the condition that
 you accept all of the terms of the GPL License.

 You may obtain a copy of the GPL License at:

 http://www.gnu.org/licenses/gpl-2.0.html

 Unless required by applicable law or agreed to in writing, software distributed under the GPL License is
 distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the GPL License for the specific language governing permissions and limitations under the GPL License.
 */
package com.dropview;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;

/**
 * Controller class for handling media animation transition.
 * Creator: Poohdish Rattanavijai
 * Date: 1/24/13
 * Time: 4:33 PM
 * Version: 1.00
 */
@SuppressWarnings("unused")
public class DropViewController {
    private static final String TAG = DropViewController.class.getSimpleName();

    /**
     * Default logging for {@link DropViewController} related functionality
     */
    protected static Log.LEVEL LOG_LEVEL = Log.LEVEL.VERBOSE;
    public static enum Position{
        TOP, BOTTOM, LEFT, RIGHT
    };

    /**
     * Default animation duration
     */
    private static final int DEFAULT_ANIM_DURATION = 600;

    /**
     * Device's screen height
     */
    private int max_height;

    /**
     * Device's screen width
     */
    private int max_width;

    /**
     * Is top media show
     */
    private boolean isTopOpen;

    /**
     * Is bottom media show
     */
    private boolean isBottomOpen;

    /**
     * Is left media show
     */
    private boolean isLeftOpen;

    /**
     * Is right media show
     */
    private boolean isRightOpen;

    /**
     * Reference to content holder
     */
    private View contentView;

    /**
     * Top media view
     */
    private View topView;

    /**
     *
     */
    private View targetAnimation;
    private int topViewSize;

    private View bottomView;
    private View leftView;
    private View rightView;

    private Context context;

    protected OnDropViewAction topMediaAction;
    protected OnDropViewAction bottomMediaAction;
    protected OnDropViewAction leftMediaAction;
    protected OnDropViewAction rightMediaAction;

    private DropViewConfigurator topDropViewConfigurator;

    /**
     *
     * @param topMediaAction
     */
    public void setTopMediaAction(OnDropViewAction topMediaAction){
        this.topMediaAction = topMediaAction;
    }

    public void setBottomMediaAction(OnDropViewAction bottomMediaAction){
        this.bottomMediaAction = bottomMediaAction;
    }

    public void setLeftMediaAction(OnDropViewAction leftMediaAction){
        this.leftMediaAction = leftMediaAction;
    }

    public void setRightMediaAction(OnDropViewAction rightMediaAction){
        this.rightMediaAction = rightMediaAction;
    }

    public static void SetDebugLevel(Log.LEVEL level){
       LOG_LEVEL = level;
    }

    public DropViewController(Context context){
        isTopOpen = false;
        isBottomOpen = false;
        isLeftOpen = false;
        isRightOpen = false;

        this.context = context;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        this.max_width = context.getResources().getDisplayMetrics().widthPixels;
        this.max_height = context.getResources().getDisplayMetrics().heightPixels;
        Log.i(TAG, "DropViewController created at " + this.max_width + "x" + this.max_height + "pixels");
    }

    public void onDestroy(){
        topView = null;
        bottomView = null;
        leftView = null;
        rightView = null;
    }

    public void setTopDropViewAnimationConfig(DropViewConfigurator topDropViewConfigurator){
        this.topDropViewConfigurator = topDropViewConfigurator;
    }

    /**
     * Generating default animation for showing DropViewNotification
     * @return {@link Animator} to use
     */
    private Animator defaultTopOpenAnimator(){
        AnimatorSet aset = new AnimatorSet();
        Log.d(TAG, "targetAnimation: " + targetAnimation.getId());
        ObjectAnimator aTop_translate = ObjectAnimator.ofFloat(targetAnimation, "translationY", -topViewSize, 0);
        ObjectAnimator aTop_alpha = ObjectAnimator.ofFloat(targetAnimation, "alpha", 1);
        aTop_translate.setInterpolator(new AccelerateDecelerateInterpolator());
        aTop_alpha.setInterpolator(new AccelerateDecelerateInterpolator());


        // Push Content down
        ObjectAnimator a2 = ObjectAnimator.ofFloat(contentView, "translationY", topViewSize);

        aset.playTogether(
                aTop_translate, aTop_alpha
                ,
                a2
        );

        aset.setDuration(DEFAULT_ANIM_DURATION);

        return aset;
    }

    /**
     * Generating default animation for dismissing DropViewNotification
     * @return {@link Animator} to use
     */
    private Animator defaultTopCloseAnimator(){
        AnimatorSet aset = new AnimatorSet();
        ObjectAnimator aTop_translate = ObjectAnimator.ofFloat(targetAnimation, "translationY", 0, -topViewSize);
        ObjectAnimator aTop_alpha = ObjectAnimator.ofFloat(targetAnimation, "alpha", 0);
        aTop_translate.setInterpolator(new AccelerateDecelerateInterpolator());
        aTop_alpha.setInterpolator(new AccelerateDecelerateInterpolator());

        // Push Content up
        ObjectAnimator a2 = ObjectAnimator.ofFloat(contentView, "translationY", topViewSize, 0);
        a2.setInterpolator(new AnticipateInterpolator());

        aset.playTogether(
                aTop_translate, aTop_alpha, a2
        );

        aset.setDuration(DEFAULT_ANIM_DURATION);

        return aset;
    }

    /**
     * Show DropViewNotification
     * @param position {@link Position} to display the view
     */
    public void show(Position position){
        if(position == Position.TOP){
            openTop();
        }
    }

    /**
     * Dismiss DropViewNotification
     * @param position {@link Position} to hide the view
     */
    public void dismiss(Position position){
        if(position == Position.TOP){
            closeTop();
        }
    }

    /**
     * Reset position of the added view to stay just outside current display port.
     * @param position {@link Position} to reset the view
     */
    public void reset(Position position){
        if(position == Position.TOP){
            resetTop();
        }
    }

    /**
     * Reset position of the top view to stay just outside current display port
     */
    private void resetTop(){
        try {
            if(topView != null){
                if(topViewSize <= 0)topViewSize = topView.getMeasuredHeight();
                View targetAnimation = (View) topView.getParent();
                Log.d(TAG, "topViewSize: " + topViewSize);

                // Animate top show
                Log.d(TAG, "animating with: " + topViewSize);
                Log.d(TAG, "targetAnimation: " + targetAnimation);
                Log.d(TAG, "targetAnimation.getLayoutParams(): " + targetAnimation.getLayoutParams());
                targetAnimation.getLayoutParams().height = topViewSize;
                targetAnimation.requestLayout();

                AnimatorSet aset = new AnimatorSet();
                ObjectAnimator aTop_translate = ObjectAnimator.ofFloat(targetAnimation, "translationY", -topViewSize);
                ObjectAnimator aTop_alpha = ObjectAnimator.ofFloat(targetAnimation, "alpha", 0);

                // Push Content down
                ObjectAnimator a2 = ObjectAnimator.ofFloat(contentView, "translationY", -topViewSize);
                a2.setInterpolator(new AnticipateInterpolator());

                aset.playTogether(
                        aTop_translate, aTop_alpha
                );

                aset.setDuration(0).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
            if(topMediaAction != null)topMediaAction.onViewAnimateException(Position.TOP, true, topView, e);
        }
    }

    private View openTop(){
        Log.d(TAG, "openTop: " + topView);
        if(topMediaAction != null)topMediaAction.onPreViewAnimate(true);

        try {
            if(topView != null && !isTopOpen){
                if(topViewSize <= 0)topViewSize = topView.getMeasuredHeight();
                targetAnimation = (View) topView.getParent();
                Log.d(TAG, "topViewSize: " + topViewSize);

                if(topDropViewConfigurator != null){
                    Log.d(TAG, "show using custom animation");
                    topDropViewConfigurator.buildOpening().start();
                }else{
                    defaultTopOpenAnimator().start();
                }

                // Animate top show
                Log.d(TAG, "animating with: " + topViewSize);
                Log.d(TAG, "targetAnimation: " + targetAnimation);
                Log.d(TAG, "topView.getTag: " + topView.getTag());
                Log.d(TAG, "targetAnimation.getTag: " + targetAnimation.getTag());

                isTopOpen = true;
            }else{
               throw new Exception("No view found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(topMediaAction != null)topMediaAction.onViewAnimateException(Position.TOP, true, topView, e);
        }

        if(topMediaAction != null)topMediaAction.onPostViewAnimate(true);

        return topView;
    }

    private void closeTop(){
        Log.d(TAG, "closeTop");
        if(topMediaAction != null)topMediaAction.onPreViewAnimate(true);

        try {
            if(topView != null && isTopOpen){
                targetAnimation = (View) topView.getParent();

                if(topDropViewConfigurator != null){
                    Log.d(TAG, "dismiss using custom animation");
                    topDropViewConfigurator.buildClosing().start();
                }else{
                    defaultTopCloseAnimator().start();
                }

                isTopOpen = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(topMediaAction != null)topMediaAction.onViewAnimateException(Position.TOP, true, topView, e);
        }

        if(topMediaAction != null)topMediaAction.onPostViewAnimate(true);
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public View getTopView() {
        return topView;
    }

    public void setTopView(View topView) {
        this.topView = topView;
    }

    public void setTopView(View topView, int height) {
        this.topView = topView;
        this.topViewSize = height;
    }

    public View getBottomView() {
        return bottomView;
    }

    public void setBottomView(View bottomView) {
        this.bottomView = bottomView;
    }

    public View getLeftView() {
        return leftView;
    }

    public void setLeftView(View leftView) {
        this.leftView = leftView;
    }

    public View getRightView() {
        return rightView;
    }

    public void setRightView(View rightView) {
        this.rightView = rightView;
    }

    public boolean isTopOpen() {
        return isTopOpen;
    }

    public void setTopOpen(boolean topOpen) {
        isTopOpen = topOpen;
    }

    public boolean isBottomOpen() {
        return isBottomOpen;
    }

    public void setBottomOpen(boolean bottomOpen) {
        isBottomOpen = bottomOpen;
    }

    public boolean isLeftOpen() {
        return isLeftOpen;
    }

    public void setLeftOpen(boolean leftOpen) {
        isLeftOpen = leftOpen;
    }

    public boolean isRightOpen() {
        return isRightOpen;
    }

    public void setRightOpen(boolean rightOpen) {
        isRightOpen = rightOpen;
    }

    protected void onViewSizeMeasured(Position position, int size, View mediaView, View contentView){
        if(position == Position.TOP && topMediaAction != null){
            topMediaAction.onViewSizeMeasure(position, size, (View)topView.getParent(), contentView);
        }
    }

    /**
     * Action Listener for {@link DropViewActivity} to trigger when each animation occurred.
     */
    public interface OnDropViewAction {

        /**
         * Callback at the start of the animation
         * @param isOpen true if opening the view
         * @return View content to be display
         */
        public View onPreViewAnimate(boolean isOpen);

        /**
         * Callback at the end of the animation
         * @param isOpen true if opening the view
         * @return View content to be display
         */
        public View onPostViewAnimate(boolean isOpen);

        /**
         * Callback when exception occurred during animation.
         * @param position that cause this exception
         * @param isOpen true if opening the view
         * @param view View that is being animated
         * @param exception thrown Exception
         */
        public void onViewAnimateException(Position position, boolean isOpen, View view, Exception exception);

        /**
         * Callback for when the view get measured.  <br />
         * This allow outside activity to gain access to view size to configure custom animation appropriately.
         * @param position Measured view's {@link Position}
         * @param size Width or height of the measured view
         * @param mediaView Notification view for animation
         * @param contentView content view for animation
         */
        public void onViewSizeMeasure(Position position, int size, View mediaView, View contentView);
    }

    /**
     * Configuration for animating media and content view transition.
     */
    public static class DropViewConfigurator {
        /**
         * Style to use in the animation. <br />
         * <b>OVERLAY</b> will animate only Rich Media on top of existing content. <br />
         * <b>PUSH</b> will animate both Rich Media and existing content.
         * This is suitable if you want the Rich Media on the screen for longer period of time.
         */
        public static enum Style{
            OVERLAY, PUSH
        };

        /**
         * For keeping track of the value for each type of the animation
         */
        public static enum Transaction{
            OPEN_MEDIA(0), CLOSE_MEDIA(1), OPEN_CONTENT(2), CLOSE_CONTENT(3);
            protected int value;
            private Transaction(int value){
                this.value = value;
            }
        };

        /**
         * Order of the animations to occur
         */
        public static enum Order{
            SYNCHRONOUS(0), SEQUENTIAL_DROPVIEW_FIRST(1), SEQUENTIAL_DROPVIEW_LAST(2), DROPVIEW_ONLY(3);
            protected int value;
            private Order(int value){
                this.value = value;
            }
        };

        /**
         * {@link Animator} object for animating DropView into the view
         */
        private Animator openingDVAnimator;

        /**
         * {@link Animator} object for animating DropView out of the view
         */
        private Animator closingDVAnimator;

        /**
         * {@link Animator} object for animating content when DropView is animated into the view
         */
        private Animator openingContentAnimator;

        /**
         * {@link Animator} object for animating content when DropView is animated out of the view
         */
        private Animator closingContentAnimator;
        private Order order = Order.SYNCHRONOUS;

        private int[] duration = {DEFAULT_ANIM_DURATION, DEFAULT_ANIM_DURATION, DEFAULT_ANIM_DURATION, DEFAULT_ANIM_DURATION};

        /**
         * Create configurator for rich media and content.
         */
        public DropViewConfigurator(){
        }

        public Animator getOpeningDVAnimator() {
            return openingDVAnimator;
        }

        /**
         * Set opening animation for Rich Media view
         * @param openingDVAnimator Animation to use
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setOpeningDVAnimator(Animator openingDVAnimator) {
            this.openingDVAnimator = openingDVAnimator;
            return this;
        }

        public Animator getClosingDVAnimator() {
            return closingDVAnimator;
        }

        /**
         * Set closing animation for Rich Media view
         * @param closingDVAnimator Animation to use
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setClosingDVAnimator(Animator closingDVAnimator) {
            this.closingDVAnimator = closingDVAnimator;
            return this;
        }

        public Animator getOpeningContentAnimator() {
            return openingContentAnimator;
        }

        /**
         * Set opening animation for content view
         * @param openingContentAnimator Animation to use
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setOpeningContentAnimator(Animator openingContentAnimator) {
            this.openingContentAnimator = openingContentAnimator;
            return this;
        }

        public Animator getClosingContentAnimator() {
            return closingContentAnimator;
        }

        /**
         * Set closing animation for content view
         * @param closingContentAnimator Animation to use
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setClosingContentAnimator(Animator closingContentAnimator) {
            this.closingContentAnimator = closingContentAnimator;
            return this;
        }

        public Order getOrder(){
            return order;
        }

        /**
         * Set order in which the animation get animated
         * @param order {@link Order} to use
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setOrder(Order order){
            this.order = order;
            return this;
        }

        /**
         * Set all animation duration
         * @param duration for use when animating
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setAllDuration(int duration){
            setOpeningMediaDuration(duration);
            setClosingContentDuration(duration);
            setOpeningContentDuration(duration);
            setClosingContentDuration(duration);
            return this;
        }

        /**
         * Set animation duration for opening media
         * @param duration for use when opening media view or both if using {@link Order}.SYNCHRONOUS
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setOpeningMediaDuration(int duration){
            this.duration[Transaction.OPEN_MEDIA.value] = duration;
            return this;
        }

        /**
         * Set animation duration for closing media
         * @param duration for use when closing media view or both if using {@link Order}.SYNCHRONOUS
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setClosingMediaDuration(int duration){
            this.duration[Transaction.CLOSE_MEDIA.value] = duration;
            return this;
        }

        /**
         * Set animation duration for content view while opening media
         * @param duration for use when opening content view
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setOpeningContentDuration(int duration){
            this.duration[Transaction.OPEN_CONTENT.value] = duration;
            return this;
        }


        /**
         * Set animation duration for content view while closing media
         * @param duration for use when closing content view
         * @return current {@link com.dropview.DropViewController.DropViewConfigurator} object
         */
        public DropViewConfigurator setClosingContentDuration(int duration){
            this.duration[Transaction.CLOSE_CONTENT.value] = duration;
            return this;
        }

        /**
         * Build opening animation from given configuration
         * @return Opening {@link Animator}
         */
        public Animator buildOpening(){
            AnimatorSet animator = new AnimatorSet();

            if(this.order == Order.SYNCHRONOUS){
                Log.d(TAG, "Synchronous opening");
                animator.playTogether(this.openingDVAnimator, this.openingContentAnimator);
                if(this.duration[Transaction.OPEN_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.OPEN_MEDIA.value]);
                }
            }else if(this.order == Order.SEQUENTIAL_DROPVIEW_FIRST){
                Log.d(TAG, "Media first opening");
                animator.playSequentially(this.openingDVAnimator, this.openingContentAnimator);
                if(this.duration[Transaction.OPEN_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.OPEN_MEDIA.value]);
                }
            }else if(this.order == Order.SEQUENTIAL_DROPVIEW_LAST){
                Log.d(TAG, "Media last opening");
                animator.playSequentially(this.openingContentAnimator, this.openingDVAnimator);
                if(this.duration[Transaction.OPEN_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.OPEN_MEDIA.value]);
                }
            }else if(this.order == Order.DROPVIEW_ONLY){
                animator.play(this.openingDVAnimator);
                if(this.duration[Transaction.OPEN_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.OPEN_MEDIA.value]);
                }
            }

            return animator;
        }

        /**
         * Build closing animation from given configuration
         * @return Closing {@link Animator}
         */
        public Animator buildClosing(){
            AnimatorSet animator = new AnimatorSet();

            if(this.order == Order.SYNCHRONOUS){
                animator.playTogether(this.closingDVAnimator, this.closingContentAnimator);
                if(this.duration[Transaction.CLOSE_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.CLOSE_MEDIA.value]);
                }
            }else if(this.order == Order.SEQUENTIAL_DROPVIEW_FIRST){
                animator.playSequentially(this.closingDVAnimator, this.closingContentAnimator);
                if(this.duration[Transaction.CLOSE_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.CLOSE_MEDIA.value]);
                }
            }else if(this.order == Order.SEQUENTIAL_DROPVIEW_LAST){
                animator.playSequentially(this.closingContentAnimator, this.closingDVAnimator);
                if(this.duration[Transaction.CLOSE_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.CLOSE_MEDIA.value]);
                }
            }else if(this.order == Order.DROPVIEW_ONLY){
                animator.play(this.closingDVAnimator);
                if(this.duration[Transaction.CLOSE_MEDIA.value] > -1){
                    animator.setDuration(this.duration[Transaction.CLOSE_MEDIA.value]);
                }
            }

            return animator;
        }

    }
}
