package juhe.jiangdajiuye.tool;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import juhe.jiangdajiuye.R;


public  class MyEdit extends EditText implements OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable,search;
    private boolean hasFoucs;
	private Context context;

	public MyEdit(Context context) {
		this(context, null);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
     public MyEdit(Context context, AttributeSet attrs) {
	    	this(context, attrs, android.R.attr.editTextStyle);
	    } 
     
     public MyEdit(Context context, AttributeSet attrs, int defStyle) {
         super(context, attrs, defStyle);
         init();
     }

     private void init() { 
     	mClearDrawable = getCompoundDrawables()[2];//获取右边的图片
		 search = getCompoundDrawables()[0];//获取左边的图片
         if (mClearDrawable == null) { 
         	mClearDrawable = getResources().getDrawable(R.drawable.delete_button);
         }
		 if (search == null) {
			 search = getResources().getDrawable(R.drawable.search_icn);
		 }
		 mClearDrawable.setBounds(-10, 0, mClearDrawable.getMinimumWidth()-35, mClearDrawable.getMinimumHeight()-25);
		 search.setBounds(20, 0, search.getMinimumWidth()-20, search.getMinimumHeight()-40);
		 setCompoundDrawables(search,null,null,null);

		 setClearIconVisible(false);
         setOnFocusChangeListener(this);
         addTextChangedListener(this);
     }
     @Override
 	public boolean onTouchEvent(MotionEvent event) {
 		if (event.getAction() == MotionEvent.ACTION_UP) {
 			if (getCompoundDrawables()[2] != null) {

 				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
 						&& (event.getX() < ((getWidth() - getPaddingRight())));
 				
 				if (touchable) {
 					this.setText("");
 				}
 			}
 		}

 		return super.onTouchEvent(event);
 	}
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void onTextChanged(CharSequence s, int start, int count,
							  int after) {
            	if(hasFoucs){
            		setClearIconVisible(s.length() > 0);
            	}
    } 
 
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
								  int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFocusChange(View arg0, boolean hasFocus) {
		// TODO Auto-generated method stub
		this.hasFoucs = hasFocus;
        if (hasFocus) { 
            setClearIconVisible(getText().length() > 0); 
        } else { 
            setClearIconVisible(true);
        } 
	}
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
		//将图片设置到指定的地方，依次是左上右下
        setCompoundDrawables(getCompoundDrawables()[0], 
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]); 
    } 
    

}
