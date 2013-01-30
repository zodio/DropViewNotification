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
import android.os.Bundle;
import android.view.*;
import android.widget.FrameLayout;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Base {@link SherlockFragmentActivity} to handle in-app view population.
 * Creator: Poohdish Rattanavijai
 * Date: 1/24/13
 * Time: 4:12 PM
 * Version: 1.00
 */
@SuppressWarnings("unused")
public class DropViewActivity extends SherlockFragmentActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = DropViewActivity.class.getSimpleName();

    /**
     * Controller for handle all transition
     */
    protected DropViewController dropViewController;

    /**
     * Listening layout changes
     */
    private ViewTreeObserver viewObserver;

    /**
     * Main layout of the view
     */
    private View mainLayout;

    /**
     * Height of the top media.
     */
    private int topSize;

    @Override
    public void setContentView(int layoutResId) {
        createMainView();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(layoutResId, null, false);

        ViewGroup vg = (ViewGroup) mainLayout.findViewById(R.id._root);
        vg.addView(view);
        dropViewController.setContentView(view);
        super.setContentView(mainLayout);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        createMainView();
        ViewGroup vg = (ViewGroup) mainLayout.findViewById(R.id._root);
        vg.addView(view, params);
        dropViewController.setContentView(view);
        super.setContentView(mainLayout, params);
    }

    @Override
    public void setContentView(View view) {
        createMainView();
        ViewGroup vg = (ViewGroup) mainLayout.findViewById(R.id._root);
        vg.addView(view);
        dropViewController.setContentView(view);
        super.setContentView(mainLayout);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dropViewController = new DropViewController(this);
    }

    @Override
    public void onGlobalLayout() {
        Log.d(TAG, "onGlobalLayout");
        View v = mainLayout.findViewById(R.id._topPane);

        if(v != null){

            /**
             * Checking if top notification height has been modified
             */
            if(v.getMeasuredHeight() > 0
                    && (topSize < 0 || topSize != v.getMeasuredHeight())){
                topSize = v.getMeasuredHeight();
                Log.d(TAG, "onGlobalLayout topSize: " + topSize);
                dropViewController.reset(DropViewController.Position.TOP);
                dropViewController.onViewSizeMeasured(DropViewController.Position.TOP
                        , topSize
                        , v
                        , mainLayout.findViewById(R.id._root));

            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onPostCreate");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        dropViewController.onDestroy();
        super.onDestroy();
    }

    /**
     * Inflate necessary layout and registering layout listener
     */
    private void createMainView(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainLayout = inflater.inflate(R.layout._rich_media_frame, null, false);
        viewObserver = mainLayout.getViewTreeObserver();
        viewObserver.addOnGlobalLayoutListener(this);
    }

    /**
     * Get DropViewController for this {@link android.app.Activity}
     * @return {@link DropViewController}
     */
    protected DropViewController getDropViewController(){
        return dropViewController;
    }

    /**
     * Set top view to display
     * @param v {@link View} to display
     */
    protected void setTopView(View v){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL| Gravity.TOP;
        setTopView(v, params);
    }

    /**
     * Set top view to display
     * @param v {@link View} to display
     * @param params {@link FrameLayout.LayoutParams} to use with the view
     */
    protected void setTopView(View v, FrameLayout.LayoutParams params){
        topSize = -1;
        v.setLayoutParams(params);

        ViewGroup vg = (ViewGroup) mainLayout.findViewById(R.id._topPane);

        /**
         * Ensuring there is maximum of only one view at all time.
         */
        if(vg.getChildCount() > 0){
            vg.removeAllViews();
        }
        vg.addView(v);
        v.requestLayout();
        dropViewController.setTopView(v, topSize);
    }
}