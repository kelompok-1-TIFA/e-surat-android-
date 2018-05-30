package com.pratamatechnocraft.e_arsip.Fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.e_arsip.Adapter.AdapterRecycleViewSuratMasuk;
import com.pratamatechnocraft.e_arsip.Model.BaseUrlApiModel;
import com.pratamatechnocraft.e_arsip.Model.ListItemSuratMasuk;
import com.pratamatechnocraft.e_arsip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuratMasukFragment extends Fragment {

    private RecyclerView recyclerViewSuratMasuk;
    private RecyclerView.Adapter adapterSuratMasuk;
    ProgressBar loading;

    private List<ListItemSuratMasuk> listItemSuratMasuks;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/surat_masuk?api=suratmasukall";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_surat_masuk_fragment, container, false);
        loading = view.findViewById(R.id.progressBarmasuk);

        recyclerViewSuratMasuk = (RecyclerView) view.findViewById(R.id.recycleViewSuratMasuk);
        recyclerViewSuratMasuk.setHasFixedSize(true);
        recyclerViewSuratMasuk.setLayoutManager(new LinearLayoutManager(getContext()));

        listItemSuratMasuks = new ArrayList<>();

        loadSuratMasuk();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("E-Arsip | Surat Masuk");
    }

    private void loadSuratMasuk(){
        loading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray suratmasuk = new JSONArray( response );

                        for (int i = 0; i<suratmasuk.length(); i++){
                            JSONObject suratmasukobject = suratmasuk.getJSONObject( i );

                            ListItemSuratMasuk listItemSuratMasuk = new ListItemSuratMasuk(
                                    suratmasukobject.getString( "id_surat_masuk"),
                                    suratmasukobject.getString( "asal_surat" ),
                                    suratmasukobject.getString( "perihal" ),
                                    suratmasukobject.getString( "tgl_arsip")
                            );

                            listItemSuratMasuks.add(listItemSuratMasuk);
                        }

                        adapterSuratMasuk = new AdapterRecycleViewSuratMasuk(listItemSuratMasuks, getContext());

                        recyclerViewSuratMasuk.setAdapter(adapterSuratMasuk);
                        loading.setVisibility(View.GONE);
                    }catch (JSONException e){
                        e.printStackTrace();
                        loading.setVisibility(View.GONE);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText( getContext(),"Error " +error.toString(), Toast.LENGTH_SHORT ).show();
                    loading.setVisibility(View.GONE);
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );


    }
}
