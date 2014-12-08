package com.dj.fragments;

import java.util.HashSet;
import java.util.Iterator;
import android.app.Fragment;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dj.virtualmyself_1.MyDb;
import com.dj.virtualmyself_1.R;

public class ExtraFragment extends Fragment implements View.OnClickListener{
	
	EditText etQues1;
	EditText etAns1;
	TextView tvKey;
	TextView tvFull;
	Button btnSave1;
	MyDb obj;
	Button btnView;
	HashSet<String> question;
	PopupWindow mpopup;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View extraView = inflater.inflate(R.layout.fragment_extra, container, false);
		
		etQues1= (EditText) extraView.findViewById(R.id.etExtraQues);
		etAns1= (EditText) extraView.findViewById(R.id.etExtraAns);
		tvFull= (TextView) extraView.findViewById(R.id.tvExtraFull);
		tvKey= (TextView) extraView.findViewById(R.id.tvExtraKey);
		btnSave1= (Button) extraView.findViewById(R.id.btnExtraSave);
		btnSave1.setOnClickListener(this);
		btnView= (Button) extraView.findViewById(R.id.btnExtraView);
		btnView.setOnClickListener(this);
		obj=new MyDb(getActivity().getBaseContext());
		question= new HashSet<String>();
		question=obj.readDbQues("SELECT Question FROM Virtual");
		tvFull.setSelected(true);
		tvKey.setSelected(true);
		obj= new MyDb(getActivity().getBaseContext());
		return extraView;
	
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		
		case R.id.btnExtraSave :
			saveClicked();
			break;
			
		case R.id.btnExtraView :
			callPopUp();
			break;
			
		case R.id.btnExtraFinish :
			mpopup.dismiss();
			break;
			
		default : break;
		
		}
		
	}
	
	
	private void callPopUp() {
		
		View popUpView = getActivity().getLayoutInflater().inflate(R.layout.popup_window,null);
		mpopup = new PopupWindow(popUpView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		mpopup.setAnimationStyle(android.R.style.Animation_Translucent);
	    mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
	    TextView tvSome = (TextView) popUpView.findViewById(R.id.tvPop);
	    tvSome.setMovementMethod(new ScrollingMovementMethod());
	    StringBuilder sb=new StringBuilder(); 
	    Iterator<String> iterate=question.iterator();
	    while(iterate.hasNext()){
	    	sb=sb.append(iterate.next()+" \n");
	    }
	    String hold=sb.toString();
	    tvSome.setText(hold);
	    Button btnCancel = (Button) popUpView.findViewById(R.id.btnExtraFinish);
	    btnCancel.setOnClickListener(this);
		
	}

	public void saveClicked(){
		
		if(!(question.contains(etQues1.getText().toString()))){
			
			if(!(etQues1.getText().toString().trim().length()==0)&&!(etAns1.getText().toString().trim().length()==0)){
				try{
					question.add(etQues1.getText().toString());
					String query="INSERT INTO Virtual(Question,Answer) VALUES("+"'"+etQues1.getText().toString()+"'"+","+"'"+etAns1.getText().toString()+"'"+")" ;
					obj.writeDb(query);
					etQues1.setText("");
					etAns1.setText("");
					Toast.makeText(getActivity().getBaseContext(),"saved successfully",Toast.LENGTH_SHORT).show();
				}catch(SQLiteException e){
					Toast.makeText(getActivity().getBaseContext(),"Error in Contacting the Database", Toast.LENGTH_LONG).show();
				}
			}else
				Toast.makeText(getActivity().getBaseContext(), "Question/Answer field cannot be blank", Toast.LENGTH_SHORT).show();	
		}
		else
			Toast.makeText(getActivity(), "Question Already exists,if you want to change the answer goto Alter Database", Toast.LENGTH_LONG).show();
	}
}
