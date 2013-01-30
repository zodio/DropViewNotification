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

/**
 * Log utility class to consolidate message logging configuration to be able to turn on or off easily at runtime.
 * Method naming honors {@link android.util.Log} for easier understanding.
 * Creator: Poohdish Rattanavijai
 * Date: 1/25/13
 * Time: 7:42 PM
 * Version: 1.00
 */
@SuppressWarnings("unused")
public class Log {
    /**
     * Global tag is pre-append on to normal log tag for easier filtering.
     */
    private static final String GLOBAL_TAG = "ROBGTHAI:";
    /**
     *  Add NONE(-1) on top of the usual {@link android.util.Log} values.
     *  When set to NONE no logging will be done.
     */
    public static enum LEVEL {
        NONE(-1), VERBOSE(2), DEBUG(3), INFO(4), WARN(5), ERROR(6), ASSERT(7);

        private int value;
        private LEVEL(int level){
            this.value = level;
        }

        public int getValue(){
            return value;
        }
    };

    public static void e(String tag, String msg){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.ERROR.getValue()){
            android.util.Log.e(GLOBAL_TAG + tag, msg);
        }
    }
    public static void e(String tag, String msg, Throwable e){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.ERROR.getValue()){
            android.util.Log.e(GLOBAL_TAG + tag, msg, e);
        }
    }
    public static void w(String tag, String msg){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.WARN.getValue()){
            android.util.Log.w(GLOBAL_TAG + tag, msg);
        }
    }
    public static void w(String tag, String msg, Throwable e){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.WARN.getValue()){
            android.util.Log.w(GLOBAL_TAG + tag, msg, e);
        }
    }
    public static void i(String tag, String msg){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.INFO.getValue()){
            android.util.Log.i(GLOBAL_TAG + tag, msg);
        }
    }
    public static void i(String tag, String msg, Throwable e){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.INFO.getValue()){
            android.util.Log.i(GLOBAL_TAG + tag, msg, e);
        }
    }
    public static void d(String tag, String msg){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.DEBUG.getValue()){
            android.util.Log.d(GLOBAL_TAG + tag, msg);
        }
    }
    public static void d(String tag, String msg, Throwable e){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.DEBUG.getValue()){
            android.util.Log.d(GLOBAL_TAG + tag, msg, e);
        }
    }
    public static void v(String tag, String msg){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.VERBOSE.getValue()){
            android.util.Log.v(GLOBAL_TAG + tag, msg);
        }
    }
    public static void v(String tag, String msg, Throwable e){
        if(DropViewController.LOG_LEVEL.getValue() <= LEVEL.VERBOSE.getValue()){
            android.util.Log.v(GLOBAL_TAG + tag, msg, e);
        }
    }
}
