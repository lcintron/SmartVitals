package usf.smartvitals.gui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class LinkClickListener implements OnClickListener {

	Context c;
	Class<?> t;
	
	public LinkClickListener(Context context, Class<?> target) {
		c = context;
		t = target;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(c, t);
		c.startActivity(intent);
	}

}
