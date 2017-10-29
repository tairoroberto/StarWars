package br.com.tairoroberto.starwars.principal.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Set;

import br.com.tairoroberto.starwars.R;
import br.com.tairoroberto.starwars.model.Person;

public class PersonAdapter extends BaseAdapter {

    private Activity activity;
    private Set<String> persons;
    private Gson gson;

    public PersonAdapter(Set<String> persons, Activity activity) {
        this.persons = persons;
        this.activity = activity;

        if(gson == null){
            gson = new Gson();
        }
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int i) {
        String s = persons.toArray()[i].toString();
        Person p = gson.fromJson(s, Person.class);

        return p;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String s = persons.toArray()[i].toString();
        Person p = gson.fromJson(s, Person.class);

        view = activity.getLayoutInflater().inflate(R.layout.adapter_person, viewGroup, false);

        TextView personName = (TextView) view.findViewById(R.id.adapter_person_tv_person_name);
        TextView user = (TextView) view.findViewById(R.id.adapter_person_tv_user);
        TextView url = (TextView) view.findViewById(R.id.adapter_person_tv_url);

        personName.setText(p.getName());
        user.setText(p.getUserCapture());
        url.setText(p.getUrl());

        return view;
    }
}
