package com.dj.myselfvirtual;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainMyselfActivity extends Activity {

	EditText etQues;
	Button btnTap;
	ImageButton btnAns;
	TextToSpeech tts;
	int res;
	ArrayList<String> result;
	String text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_myself_layout);
		etQues=(EditText) findViewById(R.id.etQuestion);
		btnTap= (Button) findViewById(R.id.btnTap);
		btnAns= (ImageButton) findViewById(R.id.btnImage);
		tts= new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
			
			@Override
			public void onInit(int status) {
				if(status==TextToSpeech.SUCCESS){
					res=tts.setLanguage(Locale.UK);
					
				}	 
			}
		});
	}
	
	public void answer(View v){
		
		answerQuery();
	}
	
	private void answerQuery(){
		
		try{
			if(result.get(0).contains("who are you")||result.get(0).contains("who is this")||result.get(0).contains("your name")){
				text="I am Dj";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if(result.get(0).equalsIgnoreCase("hello")||result.get(0).equalsIgnoreCase("hai")||result.get(0).equalsIgnoreCase("hi")){
				text="hai hello";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if((result.get(0).contains("about")||result.get(0).contains("introduce"))&&result.get(0).contains("yourself")){
				text="Myself darshan,I was born and brought up in bangalore.I completed my B.E in G.S.K.S.J.T.I... near K.R. circle."+
						"I was one of the executive members in Felcom,its a small organization by Electronics department,that conducts various technical events."+
						"My father is a BSNL Employee.I'm hardworking,dedicated,creative.I would like working as a member in a dedicated team,and i'm interested in Quantum Physics,"+
						"Cosmology,and haematology.During my free time i listen to Audio books and podcast.";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if(result.get(0).contains("your qualification")){
				text="B.E (Electronics and Commuication)";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if((result.get(0).contains("your")||result.get(0).contains("you"))&&(result.get(0).contains("interest")||result.get(0).contains("interests")||result.get(0).contains("interested")||result.get(0).contains("interesting"))){
				text="Quantum physics,cosmology and Haematology";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if(result.get(0).contains("favourite foods")||result.get(0).contains("fav food")||result.get(0).contains("favourite food")||(result.get(0).contains("food")&&result.get(0).contains("like"))){
				text="Anything which is consumable";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if((result.get(0).contains("movies")||result.get(0).contains("movie"))&&result.get(0).contains("you")&&result.get(0).contains("like")){
				text="The Amazing spider man";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if(((result.get(0).contains("expected")||result.get(0).contains("expect")||result.get(0).contains("expecting"))&&result.get(0).contains("salary"))||(result.get(0).contains("salary")&&result.get(0).contains("how much"))){
				text="initially i would expect fifteen thousand,the salary that would match the cost of living in bangalore";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if(result.get(0).contains("where")&&result.get(0).contains("you")&&(result.get(0).contains("stay")||result.get(0).contains("live"))){
				text="koramangala bangalore";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if((result.get(0).contains("father's")||result.get(0).contains("father"))&&result.get(0).contains("name")){
				text="Jaishankar";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if(result.get(0).contains("candy crush")){
				text="Doctor Sowmya";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			
			else if(result.get(0).contains("best")&&(result.get(0).contains("friend")||result.get(0).contains("friends"))){
				text="Google and those friends who really care about me";
				tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
			}
			else
				throw new Exception(); 
			
		}catch(NullPointerException ex){
			text="Ask a Question";
			tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
		}catch(Exception ex){
			text="Question your asking has no predefined answers,try asking differently..Thank You";
			tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
		}

		
		
			
			
		btnTap.setEnabled(true);
		btnTap.setText("Tap to AsK");
		
	}
	
	public void speak(View v){
		
		startVoiceRecognition();
		
	}

	private void startVoiceRecognition() {
		
		Intent speakIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		speakIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		
		startActivityForResult(speakIntent, 1);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
			if(requestCode==1 && resultCode==RESULT_OK){
				result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				etQues.setText(result.get(0));
				
			}
			btnTap.setText("Tap on image");
			btnTap.setEnabled(false);
			super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	

	
}
