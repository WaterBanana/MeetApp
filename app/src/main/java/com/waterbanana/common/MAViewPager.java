package com.waterbanana.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Eddie on 7/16/2015.
 */
public class MAViewPager extends android.support.v4.view.ViewPager{
    private boolean enabled;

    public MAViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if( !this.enabled )
            return false;

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if( !this.enabled )
            return false;

        return super.onInterceptTouchEvent( ev );
    }

    public void setPagingEnabled( boolean enabled ){
        this.enabled = enabled;
    }

    public boolean getPagingEnabled(){
        return this.enabled;
    }
}
