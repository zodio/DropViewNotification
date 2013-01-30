#DropViewNotification

In-app notification library to control custom notification view to display inside the layout with animated transition.

# Objective

This library is made to act as a boilerplate code to allow in-app notification to display in the content area.

## Overview

**DropViewNotification** consists of two main classes, DropViewActivity and DropViewController. 
[DropViewActivity](https://github.com/zodio/DropViewNotification/library/src/com/dropview/DropViewActivity.java) extends [ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock)'s **SherlockFragmentActivity** by default to utilize Nineoldandroids library to enable Animator support on pre-ICS devices.
**DropViewActivity** handle view wrapping for both content and view to be used as DropView. 

[DropViewController](https://github.com/zodio/DropViewNotification/library/src/com/dropview/DropViewController.java) controls the transition for both content and the DropView if desired.

### Usage

To use DropViewNotification you must do all of the following:
Your Activity needs to extends DropViewActivity:
	public class DefaultSampleActivity extends DropViewActivity
Set DropView to display on top of the screen
	setTopView(my_drop_view);
DropView can be shown or dismissed via DropViewController
	getDropViewController().show(DropViewController.Position.TOP);
	getDropViewController().dismiss(DropViewController.Position.TOP);

	
### Example
Default DropView showing transition. The content move down as DropView transitioning into the view.
![Default showing transition effect](https://github.com/zodio/DropViewNotification/res/default_transition_in.jpg "Default showing transition effect")

Default DropView dismissing transition. The content move up just after DropView transitioning out of the view. `AnticipateInterpolator` is added to the content to add closing effect.
![Default dismissing transition effect](https://github.com/zodio/DropViewNotification/res/default_transition_out.jpg "Default dismissing transition effect")

DropView is essentially a View object so you can put anything as long as it's a child of View. 
![ProgressBar as DropView](https://github.com/zodio/DropViewNotification/res/custom_progress.jpg "ProgressBar as DropView")

You can add your own animations as well using [DropViewConfigurator](https://github.com/zodio/DropViewNotification/library/src/com/dropview/DropViewController.java#DropViewConfigurator) and [OnDropViewAction](https://github.com/zodio/DropViewNotification/library/src/com/dropview/DropViewController.java#OnDropViewAction) interface
![Custom animation out for DropView](https://github.com/zodio/DropViewNotification/res/custom_transition_out.jpg "Custom animation out to fade DropView out but not moving it.")

**DropViewConfigurator** Allow different animation sequence via `Order` method. You can choose to synchronize the animation by using Order.SYNCHRONOUS or using other options provided.
Order.SEQUENTIAL_DROPVIEW_FIRST 
![DropView first order](https://github.com/zodio/DropViewNotification/res/custom_media_first_in.jpg "DropView first order will animate content view after DropView's animation has completed.")

Order.SEQUENTIAL_DROPVIEW_LAST 
![DropView last order](https://github.com/zodio/DropViewNotification/res/custom_media_last_in.jpg "DropView last order will animate DropView after content view's animation has completed.")

### License

* [GNU General Public License, version 2](http://www.gnu.org/licenses/gpl-2.0.html)


