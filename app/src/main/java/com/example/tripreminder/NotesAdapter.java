package com.example.tripreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    Context context;
    List<String> list;



    public NotesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.note_row,parent,false);
        NotesAdapter.ViewHolder viewHolder=new NotesAdapter.ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        //holder.txt.setText(list.get(position));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });
        /*holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        public ImageButton delete;
        public ImageButton add;
        public TextView txt;
        public RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delete=itemView.findViewById(R.id.delete_note_btn);
            add=itemView.findViewById(R.id.add_note_btn);
            txt=itemView.findViewById(R.id.note_txt);


        }





    }



}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public static String TAG="main";
    private final Context context;
    private ArrayList<Player> players;

    public MyAdapter(Context context, ArrayList<Player> players) {
        this.context=context;
        this.players=players;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.row,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        Log.i(TAG, "==============onCreateViewHolder==================");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String url=players.get(position).getImgUrl();


        holder.txtname.setText(players.get(position).getName());
        holder.txtbrief.setText(players.get(position).getBrief());
        holder.getImage(url);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,Post.class);
                intent.putExtra("name",holder.txtname.getText().toString());
                intent.putExtra("brief",holder.txtbrief.getText().toString());

                BitmapDrawable bitmap= (BitmapDrawable) holder.img.getDrawable();
                Bitmap bitmap1=bitmap.getBitmap();

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 10, bs);
                intent.putExtra("image",bs.toByteArray());



                context.startActivity(intent);

            }
        });


        Log.i(TAG, "==============onBindViewHolder==================");
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        Bitmap bitmap;

        public TextView txtname;
        public ImageView img;
        public TextView txtbrief;
        public View Layout;
        public CardView cardView;
        public LinearLayout linearLayout;

        public Bitmap bitmap1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Layout=itemView;
            txtname=itemView.findViewById(R.id.txtName);
            txtbrief=itemView.findViewById(R.id.txtbrief);
            img=itemView.findViewById(R.id.img1);
            linearLayout=itemView.findViewById(R.id.linear);
            cardView=itemView.findViewById(R.id.row);




        }
        public void getImage(String url) {

            new AsyncTaskDeom().execute(url);


        }

        public class AsyncTaskDeom extends AsyncTask<String, Void, Bitmap> {

            int index;

            @Override
            protected void onPreExecute() {


            }


            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap=null;
                try {

                    bitmap=HttpHandler.download(strings[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                if(bitmap!=null) {
                    img.setImageBitmap(bitmap);
                }else{

                    img.setImageResource(R.drawable.one);
                }


            }

        }


    }




}*/
