package com.dj.fragments;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dj.virtualmyself_1.MyDb;
import com.dj.virtualmyself_1.R;

public class HomeFragment extends Fragment implements View.OnClickListener{
	
	EditText etQues;
	Button btnTap;
	ImageButton btnAns;
	TextToSpeech tts;
	int res;
	ArrayList<String> result;
	String text;
	MyDb obj;
	HashSet<String> question;
	String selectedImagePath;
	SharedPreferences pref;
	Editor editor;
	ImageView imageView;
	static final int VOICE_RECOGNIZE=1;
	static final int GALLERY_REQUEST=2;
	static final String ERROR_TOASTER="Oops!...Image has been moved or it may have been deleted.Hence a default image has been added.Replace it with your own";
	
	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        etQues=(EditText) rootView.findViewById(R.id.etQuestion);
		btnTap= (Button) rootView.findViewById(R.id.btnTap);
		btnTap.setOnClickListener(this);
		btnAns= (ImageButton) rootView.findViewById(R.id.btnImage);
		btnAns.setOnClickListener(this);
		
		
		pref=getActivity().getApplicationContext().getSharedPreferences("path", Activity.MODE_PRIVATE);
		ActionBar actionBar = getActivity().getActionBar();
	    actionBar.setDisplayOptions(actionBar.getDisplayOptions()
	            | ActionBar.DISPLAY_SHOW_CUSTOM);
	    imageView = new ImageView(actionBar.getThemedContext());
	    imageView.setId(8086);
	    imageView.setScaleType(ImageView.ScaleType.CENTER);
	    imageView.setImageResource(R.drawable.gallery_icon);
	    ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
	            ActionBar.LayoutParams.WRAP_CONTENT,
	            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
	                    | Gravity.CENTER_VERTICAL);
	    layoutParams.rightMargin = 20;
	    imageView.setLayoutParams(layoutParams);
	    imageView.setOnClickListener(this);
	    actionBar.setCustomView(imageView);
	    
	    
	    if(!pref.getString("imagePath","default value").equals("default value")){
			
	    	File myFile = new File(pref.getString("imagePath",ERROR_TOASTER));
			if(myFile.exists())
				btnAns.setImageBitmap(BitmapFactory.decodeFile(pref.getString("imagePath",ERROR_TOASTER)));
			
			else{
				
				Toast.makeText(getActivity().getBaseContext(), ERROR_TOASTER, Toast.LENGTH_LONG).show();
				btnAns.setImageResource(R.drawable.default_image);
				
			}	
		}
	    
	    
		obj=new MyDb(getActivity().getBaseContext());
		question= new HashSet<String>();
		question=obj.readDbQues("SELECT Question FROM Virtual");
		tts= new TextToSpeech(getActivity().getBaseContext(), new TextToSpeech.OnInitListener() {
			
			@Override
			public void onInit(int status) {
				if(status==TextToSpeech.SUCCESS){
					res=tts.setLanguage(Locale.UK);
					
				}	 
			}
		});
		
		return rootView;
    }



	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btnTap:
			
			Intent speakIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			speakIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			
			startActivityForResult(speakIntent, 1);
			break;
			
		case R.id.btnImage : 
			
			answerQuery();
			break;
			
		case 8086 :	galleryClick(imageView);
			break;
			
		default: break;	
		
		}
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
			if(requestCode==VOICE_RECOGNIZE && resultCode==Activity.RESULT_OK){
				
				result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				etQues.setText(result.get(0));
				btnTap.setText("Tap on image");
				btnTap.setEnabled(false);
				
			}
			
			
			if(requestCode==GALLERY_REQUEST && resultCode==Activity.RESULT_OK){
				try{
					
					Uri selectImageUri=data.getData();
					selectedImagePath=getPath(selectImageUri);
					Bitmap pic=BitmapFactory.decodeFile(selectedImagePath);
					if(!pic.equals(null)){
						btnAns.setImageBitmap(pic);
						editor=pref.edit();
						editor.putString("imagePath",selectedImagePath );
						editor.commit();
					}
				
				}catch(NullPointerException ex){
					Toast.makeText(getActivity().getBaseContext(), "Go to default gallery area and choose a pic", Toast.LENGTH_LONG).show();
				}
				
			}
			else
				super.onActivityResult(requestCode, resultCode, data);
		
	}

	
	private void answerQuery(){
		
		try{
			
			Iterator<String> iterate=question.iterator();
			String grab;
			String answer;
			int count=0;
			if(result.get(0).equals(null))
				throw new NullPointerException();
			
			while(iterate.hasNext()){
				
				if(result.get(0).contains(grab=iterate.next())){
					answer=obj.readDbAns("SELECT Answer FROM Virtual WHERE Question="+"'"+grab+"'");
					text=answer;
					tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
					count++;
					break;
				}	
			}
			
			if(count==0){
				
				text="Question your asking does not exists in the database,add this Question and its answer by going to Insert Extras";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
				
			
			
			
		}catch(NullPointerException ex){
			text="Ask a Question";
			tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
		}

		
		
			
			
		btnTap.setEnabled(true);
		btnTap.setText("Tap to AsK");
		
	}
	
	private void galleryClick(View v){
		
		Intent galleryIntent=new Intent();
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
		galleryIntent.setType("image/*");
		startActivityForResult(Intent.createChooser(galleryIntent, "pic-select"), GALLERY_REQUEST);
	}
	
	private String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
}
	
	@Override
	public void onDestroyView() {
		
		obj.close();
		super.onDestroyView();
	}
	
    @Override
    public void onDestroy() {
    	tts.shutdown();
    	super.onDestroy();
    }
    
	
}
