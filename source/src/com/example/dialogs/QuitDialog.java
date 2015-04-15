package com.example.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.rssreader.R;

public class QuitDialog {
	Context context;
	
	public QuitDialog(Context context) {
		this.context=context;
		
	}

	public void show() {
		// TODO Auto-generated method stub
		 AlertDialog.Builder quitDialog = new AlertDialog.Builder(context);
         quitDialog.setTitle(R.string.exit_title );
        
         quitDialog.setMessage(R.string.exit_confirm);
     
         quitDialog.setPositiveButton(R.string.exit_yes, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog,int which) {
              Intent intent = new Intent(Intent.ACTION_MAIN);
                 intent.addCategory(Intent.CATEGORY_HOME);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(intent);
             }
         });
     
         quitDialog.setNegativeButton(R.string.exit_no, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
             }
         });

         quitDialog.show();
		
	}

}
