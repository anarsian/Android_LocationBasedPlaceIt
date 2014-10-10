package com.example.place_its;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseUser;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		Parse.initialize(this, "tnaU7IGIxBmsYvInrNqGA6TIcqRt9D3s5iPwPPdU", "lyMTBGBTvRJSnYe9dwWuEtJobwbOZsqnD12FOinY");
        ParseUser currentUser = ParseUser.getCurrentUser();
		//if(currentUser == null){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		//} else {
		//	Intent intent = new Intent(this, MainActivity.class);
		//	startActivity(intent);
		//}


	}

}