package com.brogrammers.agora;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class QuestionAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private QuestionController quest_controller;
	
	public QuestionAdapter(QuestionController qcontroller) {
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Need to pull from the controller the list of questions for the adapter
		this.quest_controller = qcontroller;

	}

	@Override
	public int getCount() {
		return this.quest_controller.getAllQuestions().size();
	}

	@Override
	public Object getItem(int position) {
		return this.quest_controller.getQuestionById( (long) position);
	}

	@Override
	public long getItemId(int position) {
		return this.quest_controller.getQuestionById( (long) position ).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_question, null);
		}
		
		Question question = (Question)getItem(position);
		((TextView)convertView.findViewById(R.id.questionBody)).setText(question.getBody());
		((TextView)convertView.findViewById(R.id.questionTitle)).setText(question.getTitle());
		((TextView)convertView.findViewById(R.id.questionRating)).setText(Integer.toString(question.getRating()));
		List<Long> favoritedQuestions = DeviceUser.getUser().getFavoritedQuestionIDs();
		
		if (favoritedQuestions.contains(question.getID())) {
			((ImageButton)convertView.findViewById(R.id.questionFavorite)).setImageResource(R.drawable.ic_action_rating_favoritepink);	
		} else {
			// TODO: change to grey
			((ImageButton)convertView.findViewById(R.id.questionFavorite)).setImageResource(R.drawable.ic_action_rating_favoritepink);	
		}
		
		return convertView;
	}
	

}