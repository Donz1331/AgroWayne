package agro.meteoro.wayanad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Advice_Recycler extends RecyclerView.Adapter<Advice_Recycler.ViewHolder>
{

    ArrayList<String> advices = new ArrayList<>();
    Context context;

    public Advice_Recycler(Context applicationContext, ArrayList<String> advices)
    {

        this.context = applicationContext;
        this.advices = advices;

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView advice_text;
        public ViewHolder(View itemView)
        {
            super(itemView);
            advice_text = itemView.findViewById(R.id.advice_text);
        }
    }

    @Override
    public Advice_Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advice_list, parent, false);
        Advice_Recycler.ViewHolder viewHolder = new Advice_Recycler.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        holder.advice_text.setText(advices.get(position));
    }

    @Override
    public int getItemCount()
    {
        return advices.size();
    }
}