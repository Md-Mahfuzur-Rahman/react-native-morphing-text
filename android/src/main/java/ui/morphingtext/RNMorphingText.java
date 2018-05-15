package ui.morphingtext;

import android.graphics.Color;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.Gravity;


import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.views.text.ReactFontManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.hanks.htextview.base.HTextView;
import com.hanks.htextview.evaporate.EvaporateTextView;
import com.hanks.htextview.fade.FadeTextView;
import com.hanks.htextview.fall.FallTextView;
import com.hanks.htextview.line.LineTextView;
import com.hanks.htextview.rainbow.RainbowTextView;
import com.hanks.htextview.scale.ScaleTextView;
import com.hanks.htextview.typer.TyperTextView;

public class RNMorphingText extends ViewGroupManager<ViewGroup> {

    public static final String REACT_CLASS = "RNMorphingText";

    private ThemedReactContext reactContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }


    @Override
    protected FrameLayout createViewInstance(final ThemedReactContext reactContext) {
        this.reactContext = reactContext;

        return new FrameLayout(reactContext);
    }

    @ReactProp(name = "props")
    public void setProps(FrameLayout frame, final ReadableMap props) {
        HTextView textView = (HTextView) frame.getChildAt(0);

        if (textView == null) {
            String effect = props.getString("effect");

            if (effect.equalsIgnoreCase("scale")) {
                textView = new ScaleTextView(this.reactContext.getApplicationContext());
            } else if (effect.equalsIgnoreCase("evaporate")) {
                textView = new EvaporateTextView((this.reactContext.getApplicationContext()));
            } else if (effect.equalsIgnoreCase("fall")) {
                textView = new FallTextView(this.reactContext.getApplicationContext());
            } else if (effect.equalsIgnoreCase("line")) {
                textView = new LineTextView(this.reactContext.getApplicationContext());

                ((LineTextView) textView).setAnimationDuration(props.getInt("animationDuration"));
                ((LineTextView) textView).setLineColor(Color.parseColor(props.getString("lineColor")));
                ((LineTextView) textView).setLineWidth(props.getInt("lineWidth"));
            } else if (effect.equalsIgnoreCase("typer")) {
                textView = new TyperTextView(this.reactContext.getApplicationContext());

                ((TyperTextView) textView).setTyperSpeed(props.getInt("typerSpeed"));
                ((TyperTextView) textView).setCharIncrease(props.getInt("charIncrease"));
            } else if (effect.equalsIgnoreCase("rainbow")) {
                textView = new RainbowTextView(this.reactContext.getApplicationContext());

                ((RainbowTextView) textView).setColorSpace((float) props.getInt("colorSpace"));
                ((RainbowTextView) textView).setColorSpeed((float) props.getInt("colorSpeed"));

                int[] colors = new int[props.getArray("color").size()];
                for (int i = 0;i < colors.length;i++) {
                    colors[i] = Color.parseColor(props.getArray("color").getString(i));
                }

                ((RainbowTextView) textView).setColors(colors);
            } else if (effect.equalsIgnoreCase("fade")) {
                textView = new FadeTextView(this.reactContext.getApplicationContext());

                ((FadeTextView) textView).setAnimationDuration(props.getInt("animationDuration"));
            } else {
                textView = new ScaleTextView(this.reactContext.getApplicationContext());
            }

            if (!effect.equalsIgnoreCase("rainbow")) {
                textView.setTextColor(Color.parseColor(props.getString("color")));
            }

            textView.setTextSize(props.getInt("size"));

            if (!props.getString("font").equalsIgnoreCase("")) {
                Typeface typeface = ReactFontManager.getInstance().getTypeface(props.getString("font"), 0, this.reactContext.getApplicationContext().getAssets());
                if (typeface != null) {
                    textView.setTypeface(typeface);
                }
            }

            String textAlign = props.getString("textAlign");
            if (!textAlign.equalsIgnoreCase("")) {
                switch(textAlign) {
                    case "left":
                        textView.setGravity(Gravity.LEFT);
                        break;
                    case "right":
                        textView.setGravity(Gravity.RIGHT);
                        break;
                    case "center":
                        textView.setGravity(Gravity.CENTER);
                        break;
                    default:
                        break;
                }
            }

            textView.onPreDraw();

            final HTextView animateText = textView;
            frame.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    animateText.animateText(props.getString("value"));
                }
            });

            frame.addView(textView);
        } else {
            textView.animateText(props.getString("value"));
        }
    }
}