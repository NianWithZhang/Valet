package niannian.valet.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import niannian.valet.R;
import niannian.valet.ResponseModel.ClothesResponse;
import niannian.valet.ResponseModel.SuitResponse;
import niannian.valet.View.Activity.WearSuitActivity;

public class WearSuitRecyclerViewAdapter extends RecyclerView.Adapter<WearSuitRecyclerViewAdapter.ViewHolder> {

        public Context context;
        private List<SuitResponse> suits;

        static class ViewHolder extends RecyclerView.ViewHolder{

//            public ImageView suitImage;
//            public TextView suitNameText,suitDescriptionText;

            public ViewHolder(View itemView) {
                super(itemView);

//                suitImage = (ImageView)itemView.findViewById(R.id.allSuitItemImage);
//                suitNameText = (TextView)itemView.findViewById(R.id.allSuitItemNameText);
//                suitDescriptionText = (TextView)itemView.findViewById(R.id.allSuitItemDescroptionText);
            }
        }

        public WearSuitRecyclerViewAdapter(Context context,List<SuitResponse> suits){
            this.context = context;
            this.suits = suits;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_suit_item_main,
                    parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            ImageView suitImage = (ImageView)holder.itemView.findViewById(R.id.allSuitItemImage);
            TextView suitNameText = (TextView)holder.itemView.findViewById(R.id.allSuitItemNameText);
            TextView suitDescriptionText = (TextView)holder.itemView.findViewById(R.id.allSuitItemDescroptionText);

            ClothesResponse.setImage(suitImage,SuitResponse.url(context, suits.get(position).id));
            suitNameText.setText(suits.get(position).name);
            suitDescriptionText.setText(suits.get(position).evaluation);

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(),WearSuitActivity.class);
                    intent.putExtra("id",suits.get(position).id);//((ImageView)v.findViewById(R.id.bestSuitImage)).getContentDescription().toString());
                    intent.putExtra("name",suits.get(position).name);//((TextView)v.findViewById(R.id.bestSuitNameText)).getText());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return suits.size();
        }
    }
