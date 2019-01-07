package niannian.valet.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import niannian.valet.R;
import niannian.valet.ResponseModel.ClothesResponse;

public class SuitClothesListAdapter extends RecyclerView.Adapter<SuitClothesListAdapter.ViewHolder> {

    private Context context;
    private List<ClothesResponse> clothes;

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView suitImage;

        public ViewHolder(View itemView) {
            super(itemView);

//            imageAvatar = (ImageView)itemView.findViewById(R.id.waterfall_Image);
//            nameText =(TextView) itemView.findViewById(R.id.waterfall_Name_TextView);
//            contentsText = (TextView)itemView.findViewById(R.id.waterfall_contents_TextView);
            suitImage = (ImageView)itemView.findViewById(R.id.clothesImage_wearSuit);
        }
    }

    public SuitClothesListAdapter(Context context,List<ClothesResponse> clothes){
        this.context = context;
        this.clothes = clothes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suit_clothes_list_item,
                parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClothesResponse.setImage(holder.suitImage,ClothesResponse.url(context,clothes.get(position).id));
        ((TextView)holder.itemView.findViewById(R.id.clothesNameText_wearSuit)).setText(clothes.get(position).name);
//        ListData listData = mDataList.get(position);
//        holder.imageAvatar.setImageResource(listData.getImageView());
//        holder.nameText.setText(listData.getNameText());
//        holder.contentsText.setText(listData.getContentsText());
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }
}
