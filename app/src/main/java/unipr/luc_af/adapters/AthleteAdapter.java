package unipr.luc_af.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import unipr.luc_af.chronotracker.AthleteActivities;
import unipr.luc_af.chronotracker.AthleteList;
import unipr.luc_af.chronotracker.R;
import unipr.luc_af.classes.Athlete;
import unipr.luc_af.database.interfaces.DatabaseResult;
import unipr.luc_af.holders.ListViewHolder;
import unipr.luc_af.services.Database;

public class AthleteAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private Context mContext;
    private AthleteListItemClick mItemClick = null;
    private Athlete[] mAthleteList;

    public AthleteAdapter(Context context){
        mContext = context;
        mAthleteList = new Athlete[0];
    }

    public AthleteAdapter(Context context, Athlete[] athletes, AthleteListItemClick itemClick){
        mItemClick = itemClick;
        mContext = context;
        mAthleteList = athletes;
    }

    public interface AthleteListItemClick{
        void onClick(View view, Athlete athlete);
    }

    public void setAthlets(Athlete[] athletes){
        mAthleteList = athletes;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View athleteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.athelete_list_item,parent,false);
        ListViewHolder viewHolder = new ListViewHolder(athleteView);
        return viewHolder;
    }

    public void onAthleteClick(View view, Athlete athlete){
        if(mItemClick != null){
            mItemClick.onClick(view,athlete);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.itemView.setOnClickListener((view) -> onAthleteClick(view,mAthleteList[position]));
        TextView athleteName = holder.itemView.findViewById(R.id.athlete_name);
        athleteName.setText(mAthleteList[position].name);
        TextView athleteSurname = holder.itemView.findViewById(R.id.athlete_surname);
        athleteSurname.setText(mAthleteList[position].surname);
        Chip athleteActivity = holder.itemView.findViewById(R.id.athlete_activity);
        DatabaseResult activitiesResult = (cursor)->{
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if(mAthleteList[position].activityReference == cursor.getLong(0)) {
                    athleteActivity.setText(cursor.getString(1));
                }
                cursor.moveToNext();
            }
        };
        Database.getInstance().getActivities(activitiesResult);
    }

    @Override
    public int getItemCount() {
        return mAthleteList.length;
    }
}
