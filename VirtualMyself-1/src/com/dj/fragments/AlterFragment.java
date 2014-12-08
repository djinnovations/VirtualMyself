package com.dj.fragments;

import com.dj.virtualmyself_1.MyDb;
import com.dj.virtualmyself_1.R;

import android.app.Fragment;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlterFragment extends Fragment implements View.OnClickListener{
	
	EditText etAns;
	TextView tvQues;
	Button btnNext;
	Button btnPrev;
	Button btnSave;
	MyDb obj;
	int min=1;
	int currentIndex=min;
	int max;
	int prevIn;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	
		View alterView = inflater.inflate(R.layout.fragment_alter, container, false);
		
		etAns= (EditText) alterView.findViewById(R.id.etAlterAnswer);
		etAns.setMovementMethod(new ScrollingMovementMethod());
		tvQues= (TextView) alterView.findViewById(R.id.tvAlterQues);
		tvQues.setMovementMethod(new ScrollingMovementMethod());
		
		btnNext=(Button) alterView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		btnPrev= (Button) alterView.findViewById(R.id.btnPrev);
		btnPrev.setOnClickListener(this);
		btnSave= (Button) alterView.findViewById(R.id.btnAlterSave);
		btnSave.setOnClickListener(this);
		prevIn=min;
		btnPrev.setEnabled(false);
		obj= new MyDb(getActivity().getBaseContext());
		try{
		String query="SELECT * FROM Virtual WHERE QuestionId=1";
		String answer[]=obj.readDbArr(query);
		tvQues.setText(answer[1]);
		etAns.setText(answer[2]);
		}catch(RuntimeException e){
			Toast.makeText(getActivity().getBaseContext(), "Oops!..No records found in the Table,goto Insert extras and add some questions",Toast.LENGTH_LONG).show();
			Toast.makeText(getActivity().getBaseContext(), "Oops!..No records found in the Table,goto Insert extras and add some questions",Toast.LENGTH_LONG).show();
		}
		
		return alterView;
	
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		
		case R.id.btnPrev :
			prevClick();
			break;
			
		case R.id.btnNext :
			nextClick();
			break;	
			
		case R.id.btnAlterSave :
			update();
			break;
			
		default : Toast.makeText(getActivity().getBaseContext(),"Functionality is not yet added to this button", Toast.LENGTH_LONG).show();	
			break;
		
		}
		
	}
	
	private void update() {
		
		if(!(etAns.getText().toString().trim().length()==0)){
			obj.writeDb("UPDATE Virtual SET Answer='"+etAns.getText().toString()+"'"+" WHERE QuestionId="+currentIndex);
			Toast.makeText(getActivity().getBaseContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
		
		}
		
		else
			Toast.makeText(getActivity().getBaseContext(), "Oops!..Answer field cannot be blank", Toast.LENGTH_SHORT).show();
		
	}

	public void prevClick(){
		
			max=(int) DatabaseUtils.queryNumEntries(obj.getReadableDatabase(),"Virtual");
			currentIndex--;
			
			if((currentIndex>min) && (currentIndex<max))
				btnModifier();
			
			
			if(currentIndex<=min)
				btnPrev.setEnabled(false);
			
			String query="SELECT * FROM Virtual WHERE QuestionId="+currentIndex;
			String answer[]=obj.readDbArr(query);
			tvQues.setScrollX(0);
			etAns.setScrollX(0);
			tvQues.setText(answer[1]);
			etAns.setText(answer[2]);
			
			
			prevIn=currentIndex;
		}
	
	public void nextClick(){
			try{
				max=(int) DatabaseUtils.queryNumEntries(obj.getReadableDatabase(),"Virtual");
				currentIndex++;
				
				if((currentIndex>min) && (currentIndex<max))
					btnModifier();
				
				
				if(currentIndex>=max)
					btnNext.setEnabled(false);
				
				String query="SELECT * FROM Virtual WHERE QuestionId="+currentIndex;
				String answer[]=obj.readDbArr(query);
				tvQues.setScrollX(0);
				etAns.setScrollX(0);
				tvQues.setText(answer[1]);
				etAns.setText(answer[2]);
				
				prevIn=currentIndex;
			}catch(CursorIndexOutOfBoundsException e){
				Toast.makeText(getActivity().getBaseContext(), "Oops!..Either no records found in the Table, or only one record Exists",Toast.LENGTH_LONG).show();
			}
		}
	
	public void btnModifier(){
			
			btnNext.setEnabled(true);
			btnPrev.setEnabled(true);
		}

}
