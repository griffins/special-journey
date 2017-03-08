package io.gitlab.asyndicate.uhai;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.timessquare.CalendarPickerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.gitlab.asyndicate.uhai.api.ChatService;

public class Dietary extends Fragment implements HomeAccess {

    private int meal_type;
    private TagsAdapter tagsAdapter;
    private AlertDialog dialog;
    private CalendarPickerView dialogView;
    private Date dateCurrentDate;
    private View view;
    private Home home;
    private Bitmap bitmap;
    private String file;

    @Override
    public void setHomeActivity(Home home) {
        this.home = home;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_dietery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        AppCompatSpinner spinner = (AppCompatSpinner) view.findViewById(R.id.account_type);
        List<String> list = new ArrayList<String>();
        list.add("Breakfast");
        list.add("Lunch");
        list.add("Supper");

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meal_type = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dialogView = (CalendarPickerView) LayoutInflater.from(getActivity()).inflate(R.layout.datepicker_dialog, null, false);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        dialogView.init(lastYear.getTime(), nextYear.getTime());

        ImageButton calender = (ImageButton) view.findViewById(R.id.calender);
        assert calender != null;
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog == null) {
                    dialog = new AlertDialog.Builder(getActivity()) //
                            .setTitle(R.string.date_picker_title)
                            .setView(dialogView)
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setPositiveButton("Use", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setDateCurrentDate(dialogView.getSelectedDate());
                                    setTimeCurrentTime(dateCurrentDate);
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            dialogView.fixDialogDimens();
                        }
                    });

                }
                dialog.show();
            }
        });

        ImageButton time = (ImageButton) view.findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(Dietary.this.dateCurrentDate);
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        setTimeCurrentTime(calendar.getTime());
                    }
                }, 8, 00, true);
                dialog.show();
            }
        });

        ImageButton tag = (ImageButton) view.findViewById(R.id.add_tag);
        RecyclerView tags = (RecyclerView) view.findViewById(R.id.tagView);
        AutoSpanGridLayoutManager layoutManager = new AutoSpanGridLayoutManager(getActivity(), 8, 0);

        assert tag != null;
        tags.setLayoutManager(layoutManager);
        tagsAdapter = new TagsAdapter();
        tags.setAdapter(tagsAdapter);

        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View inputView = LayoutInflater.from(getActivity()).inflate(R.layout.input_text, null);

                final EditText input = (EditText) inputView.findViewById(R.id.input_text);

                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setNegativeButton("Dismiss", null).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tagsAdapter.add(new Tag(input.getText().toString().trim().toLowerCase(), getActivity()));
                    }
                }).create();
                dialog.setView(inputView);
//                dialog.setIcon(R.drawable.tag);
                dialog.setTitle("Add Person");
                dialog.show();
            }
        });
        setDateCurrentDate(null);
        setTimeCurrentTime(null);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Canceled", Snackbar.LENGTH_SHORT).show();
                home.getSupportFragmentManager().beginTransaction().replace(R.id.container, home.home).commit();
                home.setTitle("Home");
                home.currentPage = 0;
                home.invalidateOptionsMenu();
            }
        });

        Button send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    Intent intent = new Intent(getActivity(), ChatService.class);
                    intent.setAction("photo");
                    intent.putExtra("file", file);
                    getContext().startService(intent);
                }
                Snackbar.make(view, "Sent to dietitian", Snackbar.LENGTH_SHORT).show();
                home.getSupportFragmentManager().beginTransaction().replace(R.id.container, home.home).commit();
                home.setTitle("Home");
                home.currentPage = 0;
                home.invalidateOptionsMenu();
            }
        });

        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show();
                home.getSupportFragmentManager().beginTransaction().replace(R.id.container, home.home).commit();
                home.setTitle("Home");
                home.currentPage = 0;
                home.invalidateOptionsMenu();
            }
        });
    }

    public void setDateCurrentDate(Date dateCurrentDate) {
        if (dateCurrentDate == null) {
            dateCurrentDate = new Date();
        }
        this.dateCurrentDate = dateCurrentDate;
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d MMMM");
        ((TextView) view.findViewById(R.id.text_date)).setText(formatter.format(dateCurrentDate));
    }

    public void setTimeCurrentTime(Date dateCurrentDate) {
        if (dateCurrentDate == null) {
            dateCurrentDate = new Date();
        }
        this.dateCurrentDate = dateCurrentDate;
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        ((TextView) view.findViewById(R.id.text_time)).setText(formatter.format(dateCurrentDate));
    }

    public void loadImage(String file) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        final ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        file = file.replaceAll("^file://", "");
        file = "/sdcard/Android/data/io.gitlab.asyndicate.uhai/cache/sausage.jpg";
        this.file = file;
        Settings.getInstance().LoadImage(file, width, new PayloadRunnable() {
            @Override
            public Object run(Object result) {
                bitmap = (Bitmap) result;
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                return null;
            }
        });
    }
}
