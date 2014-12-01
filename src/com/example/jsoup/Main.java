package com.example.jsoup;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;


public class Main extends Activity {
	
int pageNumber = 1;	
private boolean loading = true;
String AudioLink = "";
String streamingLink;


static ArrayList<String> mThumb = new ArrayList<String>();
static ArrayList<String> mTitle = new ArrayList<String>();
static ArrayList<String> mArtist = new ArrayList<String>();
static ArrayList<String> mLink = new ArrayList<String>();
static ArrayList<String> mAudioLink = new ArrayList<String>();
static ArrayList<String> mRelease = new ArrayList<String>();
public static ArrayList<String> mData = new ArrayList<String>();
public static ListView mListView;

//Make Custom adapter a global variable
public CustomAdapter customAdapter;
public AlphaInAnimationAdapter animAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mListView = (ListView) findViewById(R.id.listView1);
		mListView.setDivider(null);
		customAdapter = new CustomAdapter(Main.this, mData); //Set custom adapter to data set and to CustomAdapter.java
		
	    animAdapter = new AlphaInAnimationAdapter(customAdapter);
	    // Assign the ListView to the AnimationAdapter and vice versa
	    animAdapter.setAbsListView(mListView);
	    mListView.setAdapter(animAdapter);
		
		//Don't set regular customAdapter otherwise, animations will not go through 
//		mListView.setAdapter(customAdapter);
		new AsyncDerp().execute();
	}

	
public class AsyncDerp extends AsyncTask<Void, Void, Void>	{
	ProgressDialog dialog = new ProgressDialog(Main.this);
//	CustomAdapter customAdapter = new CustomAdapter(Main.this, mData);
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		this.dialog.setCancelable(true);
		this.dialog.setMessage("Fetching the most up-to-date list..., Please wait.....");
		this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.dialog.setProgress(0);
		this.dialog.setMax(1000);
		this.dialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		processData();
		return null;
	}
	
	private void processData() {
//		String URL = "http://stackoverflow.com/questions/tagged/android";
		//Test music URL with HTTP Structure for JSOUP to Decompose
		String URL = "http://www.audiomack.com/trending/page/1";
		pageNumber = 1;
		 try {
			 	Document doc = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10").get();
	            Elements questions = doc.select(".track img[alt]");
//	            Elements questions2 = doc.select(".podnew odd h2 a");
	            Elements artist1 = doc.select(".track p a[href*=/search]");
//	            Elements artist2 = doc.select(".podnew odd p a[href*=/search]");
	            Elements images1 = doc.select(".track img[abs:src]");
//	            Elements images2 = doc.select(".podnew odd img[src]");
	            Elements days1 = doc.select(".track p:contains(Released:)");
	            Elements sourceLink = doc.select(".cover a[href*=/");
	            System.out.println("Elements cached from URL");
//	            System.out.println(doc);
	            for(Element question: questions) {
	                mTitle.add(question.attr("alt"));
	                mData.add(question.attr("alt"));
	            }
	            
//	            for(Element question2: questions2) {
//	            	mTitle.add(question2.text());
//	            }
	            
	            for(Element art : artist1) {
	            	mArtist.add(art.text());
	            }
//	            for(Element link2 : artist2) {
//	            	mArtist.add(link2.text());
//	            }
	            
	            for(Element img : images1) {
	            	mThumb.add(img.attr("src"));
	            }
	                   
//	            for(Element img2 : images2) {
//	            	mThumb.add(img2.attr("src"));
//	            }
	            
	            for( Element dayz1 : days1 ) {
//	            	Node nodeYouWant = dayz1.nextSibling();
	                mRelease.add(dayz1.child(4).nextSibling().toString().replace("&nbsp;", "").replace("Released: ", "").trim());
//	                System.out.println("" + nodeYouWant.toString().replace("&nbsp;", "").trim());
	            }
	            
	            for(Element refLink : sourceLink) {
	            	mLink.add(refLink.attr("href"));
//	            	System.out.println(mLink);
	            }
	            
	            if(mTitle.size() == 0) {
	                mTitle.add("Empty result");
	            }
	 
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            mTitle.clear();
	            mTitle.add("Exception: " + ex.toString());
	        }
	    }
	
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	

//Initialize endless scrolling, for when you reach bottom of page
		mListView.setOnScrollListener(new EndlessScrollListener() {	
		});	
		
		mListView.setOnItemClickListener(new listItemClick());
		
		
        //Set adapter to send data to listView once data is retrieved 
//		CustomAdapter customAdapter = new CustomAdapter(Main.this, R.layout.list_row, mData);
		if (customAdapter == null) {
			CustomAdapter customAdapter = new CustomAdapter(Main.this, mData);
			mListView.setAdapter(customAdapter);
		} else {
		//Disable setting the adapter again... otherwise you lose scroll position
//		mListView.setAdapter(customAdapter);
		customAdapter.notifyDataSetChanged();
        }
		this.dialog.dismiss();
	}
}
String  songURL = "http://www.audiomack.com";
public class listItemClick implements OnItemClickListener {
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  Toast.makeText(getApplicationContext(),
			      "http://www.audiomack.com" + mLink.get(position), Toast.LENGTH_LONG).show();
		songURL = "";
		songURL = "http://www.audiomack.com";
		songURL = songURL + mLink.get(position);
		System.out.println(songURL);
		new FindAudioSource().execute();
	}
	
}

public class FindAudioSource extends AsyncTask<Void, Void, Void>	{
	ProgressDialog grabAudioDialog = new ProgressDialog(Main.this);
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		grabAudioDialog.setMessage("Grabbing audio... please wait...");
//		grabAudioDialog.show();
		
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		grabAudioData();
		return null;
	}
	
	private void grabAudioData() {
		try {
			
			Document doc = Jsoup.connect(songURL).userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10").get();
			Elements audioID = doc.select("div audio[src]");
			for(Element finalLink : audioID) {
				AudioLink = "";
//				streamingLink = "" + finalLink; 
				mAudioLink.add(finalLink.attr("src"));
				AudioLink = mAudioLink.toString();
				System.out.println(AudioLink);
				mAudioLink.clear();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error " + e);
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		grabAudioDialog.dismiss();
//		System.out.println(streamingLink);
	}
	
	
}



public class EndlessScrollListener implements OnScrollListener {

    private int visibleThreshold = 5;
//    private int currentPage = 0;
    private int previousTotal = 0;
//    private boolean loading = true;

    public EndlessScrollListener() {
    }
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
//                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
        	new loadMoreListView().execute();
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}



public class loadMoreListView extends AsyncTask<Void, Void, Void> {
	String mackURL = "http://www.audiomack.com/trending/page/##";
//	int pageNumber = 1;
	ProgressDialog loadMoreDialog = new ProgressDialog(Main.this);
//	CustomAdapter customAdapter = new CustomAdapter(Main.this, mData);
	//If unable to save scroll position, force list to save scroll position
//	int fistVisiblePosition = mListView.getFirstVisiblePosition();
	@Override
	protected void onPreExecute() {
		loadMoreDialog.setMessage("Loading more music... please wait...");
		loadMoreDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadMoreDialog.setProgress(0);
		loadMoreDialog.setMax(1000);
//		loadMoreDialog.show();
		super.onPreExecute();
		
	}
	@Override
	protected Void doInBackground(Void... params) {
		processData();
		return null;
	}
	
	private void processData() {	
		pageNumber = pageNumber +1;
		mackURL = mackURL.replace("##", "" + String.valueOf(pageNumber));
		System.out.println(mackURL);
		
		try {
		 	Document doc = Jsoup.connect(mackURL).userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10").get();
            Elements title1 = doc.select(".podnew  h2 a");
//            Elements title2 = doc.select(".podnew odd h2 a");
            Elements artist1 = doc.select(".podnew p a[href*=/search]");
//            Elements artist2 = doc.select(".podnew odd p a[href*=/search]");
            Elements images1 = doc.select(".podnew img[src]");
//            Elements images2 = doc.select(".podnew odd img[src]");
            Elements days1 = doc.select(".podnew p strong:contains(Released)");
            Elements sourceLink = doc.select(".podnew a[href].playit:contains(Play)");
            System.out.println("Elements cached from URL");
            for(Element question: title1) {
                mTitle.add(question.text());
                mData.add(question.text());
            }
            
//            for(Element question2: title2) {
//            	mData.add(question2.text());
//            }
            
            for(Element art : artist1) {
            	mArtist.add(art.text());
            }
            
//            for(Element link2 : artist2) {
//            	mArtist.add(link2.text());
//            }
            
            for(Element img : images1) {
            	mThumb.add(img.attr("src"));
//            	System.out.println("\nsrc : " + img.attr("src")); 	
            }
                   
//            for(Element img2 : images2) {
//            	mThumb.add(img2.attr("src"));
//            	System.out.println("\nsrc : " + img2.attr("src")); 
//            }
            
            for( Element dayz1 : days1 ) {
//            	Node nodeYouWant = dayz1.nextSibling();
                mRelease.add(dayz1.nextSibling().toString().replace("&nbsp;", "").trim());
//                System.out.println("" + nodeYouWant.toString().replace("&nbsp;", "").trim());
            }   
            
            for(Element refLink : sourceLink) {
            	mLink.add(refLink.attr("href"));
//            	System.out.println(mLink);
            }

            if(mTitle.size() == 0) {
                mTitle.add("Empty result");
            }
 
        } catch (Exception ex) {
            ex.printStackTrace();
            mTitle.clear();
            mTitle.add("Exception: " + ex.toString());
         	}
	    }
	
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (customAdapter == null) {
			CustomAdapter customAdapter = new CustomAdapter(Main.this, mData);
			mListView.setAdapter(customAdapter);
			customAdapter.notifyDataSetChanged();
		} else {
			//Disable setting the adapter again... otherwise you lose scroll position
//			mListView.setAdapter(customAdapter);
			customAdapter.notifyDataSetChanged();
        }
		loadMoreDialog.dismiss();
		loading = false;
		//If unable to save scroll position force list to move to last Visible scroll position
//		mListView.setSelection(fistVisiblePosition);
		Log.d("Async", "Load More Items Completed");
	}
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
