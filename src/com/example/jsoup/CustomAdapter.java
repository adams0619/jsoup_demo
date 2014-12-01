package com.example.jsoup;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class CustomAdapter extends ArrayAdapter<String> {
	
	 Context mContext;
	 public CustomAdapter(Context context, ArrayList<String> items) {
		 super(context, R.layout.list_row, items);
		 mContext = context;
	 }
	
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		 return getViewOptimize(position, convertView, parent);
	 }
	
	public View getViewOptimize(int position, View convertView, ViewGroup parent) {
	 
	ViewHolder holder;
	  //Re-Use existing view otherwise create a new View for passing data to
	  if (convertView == null) {
		  LayoutInflater mInflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   convertView = mInflater.inflate(R.layout.list_row, null);
	   holder = new ViewHolder();
	   holder.songTitle = (TextView) convertView.findViewById(R.id.title);
	   holder.artist = (TextView) convertView.findViewById(R.id.artist);
	   holder.releaseDate = (TextView) convertView.findViewById(R.id.releasedate);
	   holder.thumbnail = (ImageView) convertView.findViewById(R.id.list_image);
	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	
	  holder.songTitle.setText(Main.mData.get(position));
	  holder.artist.setText("Artist: " + Main.mArtist.get(position));
	  holder.releaseDate.setText("Released: " + Main.mRelease.get(position));
	  Picasso.with(getContext()).load(Main.mThumb.get(position)).into(holder.thumbnail);
//	  System.out.println("ConvertView completed... no data passed");
	  return convertView;
	 }	
	
	//View-holder pattern for changing list-attributes	
	 public static class ViewHolder {
	  TextView songTitle;
	  TextView artist;
	  TextView releaseDate;
	  ImageView thumbnail;
	 }
	
}

