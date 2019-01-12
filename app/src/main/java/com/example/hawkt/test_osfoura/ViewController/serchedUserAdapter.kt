package com.example.hawkt.test_osfoura

import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.twitter.sdk.android.core.models.User
import java.text.NumberFormat
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.parcel.Parcelize
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList



const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
 var Users = ArrayList<User>()
class serchedUserAdapter(val users: List<User>) :RecyclerView.Adapter<serchedUserAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view :View = LayoutInflater.from(p0.context).inflate(R.layout.searched_users,p0,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
         p0.screenName.text= users[p1].name
         p0.handle.text="@"+ users[p1].screenName
        if((users[p1].followersCount>=1000) && (users[p1].followersCount<10000)){
            p0.numberOfFollowers.text= NumberFormat.getInstance().format(users[p1].followersCount)+ " followers"}
        else if((users[p1].followersCount>=10000)&&(users[p1].followersCount<1000000))
        {
            p0.numberOfFollowers.text= (users[p1].followersCount/1000).toString()+"K"+ " followers"
        }
        else if(users[p1].followersCount>=1000000){

            p0.numberOfFollowers.text= (users[p1].followersCount/1000000).toString()+"M"+ " followers"}

        else{
            p0.numberOfFollowers.text= users[p1].followersCount.toString()+ " followers"}
//         p0.userIcon.setImageURI(Uri.parse(users[p1].profileImageUrl))

         Picasso.get().load(users[p1].profileImageUrl).into(p0.userIcon)
        if(users[p1].verified){
           var  VerifiedImage = ImageView( p0.itemView.context)
            VerifiedImage.setImageResource(R.drawable.ic_verified_medium)
            VerifiedImage.adjustViewBounds = true
            var params : (RelativeLayout.LayoutParams) = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
             params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//           params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.user_icon)
            VerifiedImage.layoutParams=params
            p0.iconRelativeView.addView(VerifiedImage)
        }
        p0.itemView.setOnClickListener {
//            var message="Killing me softely"  ....fnn

//         Users.add(users[p1])
//
//            val intent =  Intent(p0.itemView.context,UserActivity::class.java)
//            p0.itemView.context.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val screenName: TextView = itemView.findViewById(R.id.screen_name)
        val handle: TextView = itemView.findViewById(R.id.handle)
        val numberOfFollowers:TextView = itemView.findViewById(R.id.number_of_followers)
        val userIcon: CircleImageView = itemView.findViewById(R.id.user_icon)
        var iconRelativeView: RelativeLayout = itemView.findViewById(R.id.icon_Relative_View)
    }

}