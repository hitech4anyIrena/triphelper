package com.hitech4any.triphelper.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.interfaces.THAdapterListener;
import com.hitech4any.triphelper.model.Place;
import com.hitech4any.triphelper.model.Values;

import java.util.ArrayList;

/**
 * Created by hitech4any on 21/09/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {
    /*
    This adapter is used in PlaceListFragment and in FavoritesListFragment
    Implementation of different actions of each fragment - through THAdapterListener
    */
    private Context context;
    private ArrayList<Place> places;
    private THAdapterListener listener;
    private CardView card;
    private TextView TVName;
    private TextView TVAddress;
    private TextView TVDistance;
    private RatingBar ratingBar;
    private ImageView imgIcon;
    private ImageView imgInfo;
    private ImageView imgDelete;
    private ImageView imgFv;

    public ListAdapter(Context context) {
        this.context = context;
        this.places = new ArrayList<>();
        this.listener = (THAdapterListener) context;
    }

    public ListAdapter(Context context, ArrayList<Place> places) {
        this.context = context;
        this.places = places;
        this.listener = (THAdapterListener) context;
    }

    public void add(Place place) {
        this.places.add(place);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Place> places) {
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    public void remove(Place place) {
        places.remove(place);
        notifyDataSetChanged();
    }

    public void clear() {
        this.places.clear();
        notifyDataSetChanged();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    // This method is using to redirect listeners to execute fragments through interface
    public void onBindViewHolder(final ListHolder holder, final int position) {
        holder.bind(this.places.get(position));
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(context, card);
                popup.inflate(R.menu.context_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.shareMenu:
                                Place placeToShare = getItem(position);
                                if (placeToShare != null) {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "It's interesting place: " + placeToShare.getName() + ", " + placeToShare.getAddress() + ".");
                                    sendIntent.setType("text/plain");
                                    context.startActivity(sendIntent);
                                }
                                break;
                            case (R.id.addToFvMenu):
                                if (listener != null) {
                                    listener.onFavoriteClick(position);
                                }
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onInfClick(position);
                }
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteClick(position);
                }
            }
        });
        imgFv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onFavoriteClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.places.size();
    }

    public Place getItem(int position) {
        return places.get(position);
    }

    //Holder

    public class ListHolder extends RecyclerView.ViewHolder {

        public ListHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            TVName = itemView.findViewById(R.id.TVName);
            TVAddress = itemView.findViewById(R.id.TVAddress);
            TVDistance = itemView.findViewById(R.id.TVDistance);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imgInfo = itemView.findViewById(R.id.imgInfo);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgFv = itemView.findViewById(R.id.imgFv);

        }

        public void bind(Place place) {

            TVName.setText(place.getName() + "");
            TVAddress.setText(place.getAddress() + "");
            TVDistance.setText(place.getStringDistance(listener.getLocationLat(), listener.getLocationLon(), listener.getMeasure().equalsIgnoreCase(Values.DIF_MEASURE)));
            ratingBar.setRating((float) place.getRating());
            if (place.getIcon().isEmpty()) {
                imgIcon.setImageURI(Uri.parse("android.resource://com.hitech4any.triphelper/drawable/generic_business71"));
            } else {
                imgIcon.setImageURI(Uri.parse("android.resource://com.hitech4any.triphelper/drawable/" + place.getIcon()));
            }
        }

    }
}

