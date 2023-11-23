package com.example.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.GeneralFunction.GeneralFunction;
import com.example.myapplication.R;
import com.example.myapplication.modal.Comment;
import com.example.myapplication.modal.Story;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    List<Comment> list;
    View view;


    public CommentAdapter.IclickDetail clickItem;
    public  CommentAdapter.iClickReplies clickReplies;
    public   interface  IclickDetail{
        void handleReportComment(Comment comment);
        void updateComment(Comment comment);
        void delete(Comment comment);
        void collapseComment(ArrayList<String>listCommentChild,String _id,RecyclerView rcv);
        void handleMoreIconClick(Comment comment,LinearLayout layoutTong,LinearLayout layoutSua,LinearLayout layoutXoa,LinearLayout layoutBaoCao);
    }
    public  interface  iClickReplies{
        void replyComment(Comment comment);
    }
    public  CommentAdapter (CommentAdapter.iClickReplies clickReplies){
        this.clickReplies=clickReplies;
    }
    public CommentAdapter(CommentAdapter.IclickDetail clickItem) {
        this.clickItem = clickItem;
    }
    public  void setData(List<Comment>commentList){
        this.list=commentList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment,parent,false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.text.setText(comment.getText());
        List<String>listReplies = comment.getReplies();
        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickItem!=null){
                    clickItem.handleMoreIconClick(comment,holder.layoutTong,holder.layoutXua,holder.layoutXoa,holder.layoutBaoCao);

                }


            }
        });
        holder.layoutReplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickItem!=null){
                    clickItem.collapseComment(comment.getReplies(),comment.get_id(),holder.rcv_replies);

                }
            }
        });

        if( listReplies !=null && listReplies.size()!=0){
            holder.tv_quantity_replies.setText("xem " + listReplies.size()+" câu trả lời");
        }else {
            holder.layoutReplies.setVisibility(View.GONE);
        }
        if(comment.getNameParentComment() !=null){
            holder.tv_name_replies.setText(comment.getNameParentComment());
        }
        holder.layoutBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickItem!=null){
                    clickItem.handleReportComment(comment);
                }
            }
        });
        holder.layoutXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickItem!=null){
                    clickItem.delete(comment);

                }
            }
        });
        GeneralFunction.handleTimesDiff(String.valueOf( comment.getCreatedAt()),holder.tg);
        holder.layoutXua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickItem!=null) {
                    clickItem.updateComment(comment);
                }
            }
        });
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickReplies!=null && comment!=null){
                    clickReplies.replyComment(comment);

                }


            }
        });
        holder.fullname.setText(comment.getFullName());
        Picasso.get().load(comment.getAvatar()).into(holder.img_avatar);

    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }
    public  class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView text,reply,tg,fullname,tv_quantity_replies,tv_name_replies;
        CircleImageView img_avatar;
         RecyclerView rcv_replies;
        LinearLayout layoutReplies;
        ImageView img_more;
        LinearLayout layoutXoa,layoutXua,layoutBaoCao,layoutTong;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutXoa = itemView. findViewById(R.id.linear_xoa_binh_luan);
            layoutXua = itemView. findViewById(R.id.linear_sua_binh_luan);
            layoutBaoCao = itemView. findViewById(R.id.linear_bao_cao_binh_luan);
            layoutTong = itemView.findViewById(R.id.layout_tong);
            img_more = itemView. findViewById(R.id.moreicon);
            text=itemView.findViewById(R.id.tv_text);
            tg=itemView.findViewById(R.id.tv_thoi_gian);
            reply=itemView.findViewById(R.id.tv_reply);
            tv_name_replies = itemView.findViewById(R.id.tv_name_replies);
            layoutReplies = itemView.findViewById(R.id.layout_replies);
            tv_quantity_replies=itemView.findViewById(R.id.tv_quantity_replies);
            rcv_replies = itemView.findViewById(R.id.rcv_replies);
            fullname= itemView.findViewById(R.id.tv_fullname);

            img_avatar=itemView.findViewById(R.id.img_avatar);
        }
    }
}
