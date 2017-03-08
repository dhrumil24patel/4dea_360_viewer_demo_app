package com.fourdea.a360viewerdemoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by dhrumil on 8/11/2016.
 */
public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.Holder> {

    Context context;
    ArrayList<ThumbnailBean> mainList;
    ThumbnailListener thumbnailListener;

    public ThumbnailsAdapter(Context context, ThumbnailListener thumbnailListener, ArrayList<ThumbnailBean> mainList){
        this.context = context;
        this.thumbnailListener = thumbnailListener;
        this.mainList = mainList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.my_thumbnail_item, null);

        Holder holder = new Holder(v);

        new ImageSetterTask(viewType, holder.getImageView()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return mainList.get(position).sceneNum;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ThumbnailBean bean = mainList.get(position);

        if(bean.selected){
            holder.imageView.setBackgroundColor(Color.WHITE);
        }
        else{
            holder.imageView.setBackgroundColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        public Holder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.my_thumbnail_item_image);
            itemView.setOnClickListener(this);
            itemView.setBackgroundColor(Color.WHITE);
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            int sceneNum = mainList.get(position).sceneNum;
            String sceneName = mainList.get(position).sceneName;
            try {
                thumbnailListener.onThumbnailClicked(sceneNum, sceneName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mainList.get(position).selected = true;
            for(int i=0; i<mainList.size(); i++){
                if(i != position){
                    mainList.get(i).selected = false;
                }
            }
            notifyDataSetChanged();
        }
    }


    class ImageSetterTask extends AsyncTask<Void, Void, Bitmap>{

        int sceneNum;
        ImageView imageView;
        public ImageSetterTask(int sceneNum, ImageView imageView){
            this.sceneNum = sceneNum;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            String panoImg = null;
            String shortUrl = null;
            try {
                String url = thumbnailListener.getThumbUrl(sceneNum);
                shortUrl = url.replace("/","-");
                shortUrl = shortUrl.replace(":","-");
                MyImageDownloader downloader = new MyImageDownloader(context);
                bitmap = downloader.downloadImageWithCaching(context, url, shortUrl, sceneNum, "thumb");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

}
