package tecnologo.com.sic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tecnologo.com.sic.Utilidades.Sesion;


public class Login extends FragmentActivity {

     static Login login;

    private static final String LOG_TAG = "Login_Facebook" ;

    private LoginButton loginBtn;

    private TextView userName;

    private UiLifecycleHelper uiHelper;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
     private Facebook facebook;

    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    private String get_id;
    private String get_name;
    private String get_gender;
    private String get_email;
    private String get_birthday;
    private String get_locale;
    private Session s;
    private Button btn_invitado;
    private Button btn_registrarse;
    private Button btn_iniciar_sesion;
    private Context context =this;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if(!SplashScreenActivity.splash.sesion.isLoggedIn()){
        super.onCreate(savedInstanceState);

        login = this;
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);


        //***********************************************
        btn_invitado = (Button) findViewById(R.id.btn_invitado);
        btn_invitado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getApplicationContext(), Main.class));
                onBackPressed();
            }
        });

        //***********************************************************
        btn_registrarse = (Button) findViewById(R.id.btn_registrarse);
        btn_registrarse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new Alta_Usuario()).commit();

            }

        });

        btn_iniciar_sesion = (Button) findViewById(R.id.btn_iniciar_sesion);
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                 /* Alert Dialog Code Start*/
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Login"); //Set Alert dialog title here

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView usuario = new TextView(context);
                usuario.setText("Usuario");

                layout.addView(usuario);

                final EditText campo_usuario = new EditText(context);
                campo_usuario.setHint("ingrese usuario");
                layout.addView(campo_usuario);

                final TextView password = new TextView(context);
                password.setText("Password");

                layout.addView(password);

                final EditText campo_password = new EditText(context);
                campo_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                campo_password.setHint("");


                campo_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                campo_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                layout.addView(campo_password);

                final TextView control = new TextView(context);
                control.setText("");
                control.setInputType(View.INVISIBLE);
                layout.addView(control);

                alert.setView(layout);

                alert.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });

                alertDialog = alert.create();
                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;
                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if (wantToCloseDialog) Log.d("", "");
                            //dismiss();
                        else {


                            if ((campo_password.length() == 0) || (campo_usuario.length() == 0)) {

                                control.setText("Debe ingresar usuario/password!!!");
                                control.setTextColor(Color.RED);
                                control.setGravity(Gravity.CENTER);
                                control.setVisibility(View.VISIBLE);


                            } else {


                                try {
                                    String str_usuar = campo_usuario.getEditableText().toString();
                                    String str_pass = Main.main.codificar_password(campo_password.getEditableText().toString());

                                    ArrayList<String> parametros = new ArrayList<String>();
                                    parametros.add(str_usuar);
                                    parametros.add(str_pass);
                                    AsyncTask<ArrayList<String>, Void, Boolean> get_usuario = new GetUsuario().execute(parametros);
                                    try {
                                        Boolean resultado = get_usuario.get();
                                        if (resultado) {

                                            onBackPressed();

                                        } else {
                                            control.setText("Error al ingresar usuario/password!!!");
                                            control.setTextColor(Color.RED);
                                            control.setGravity(Gravity.CENTER);
                                            control.setVisibility(View.VISIBLE);
                                        }
                                    } catch (Exception e) {


                                    }


                                } catch (NoSuchAlgorithmException e) {
                                    Log.e("Error", e.toString());

                                }
                            }


                        }
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });

                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Boolean wantToCloseDialog = false;
                        //Do stuff, possibly set wantToCloseDialog to true then...
                        // if(wantToCloseDialog)
                        //alertdismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });

                /*alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton*/


            }

        });
        /*btn_Calendario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                anio = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                input_fecha.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, anio, mes, dia);
                dpd.show();
            }
        });


        fragmento = new Alta_Usuario();*/
        //**********************************************
        //****************************************************

        // loginBtn.getSessionStatusCallback(){}
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    if (!SplashScreenActivity.splash.sesion.isLoggedIn()) {
                        Log.d("Usuario: ", user.getInnerJSONObject().toString());
                        // Log.d("USER","setUserInfoChangedCallback");
                        ArrayList<String> datos = new ArrayList<String>();
                        //get_id = user.getId();
                        datos.add(user.getFirstName());
                        datos.add(user.getLastName());
                        datos.add(user.getName());
                        datos.add(user.getProperty("gender").toString().toUpperCase());
                        /*get_gender = (String) user.getProperty("gender");
                        get_email = (String) user.getProperty("email");
                        get_birthday = user.getBirthday();
                        get_locale = (String) user.getProperty("locale");*/

                        //get_location = user.getLocation().toString();
                        //Toast.makeText(getApplicationContext(), "Usuario: "+get_name+ " Email:" +get_email, Toast.LENGTH_SHORT).show();
                        /*Log.d(LOG_TAG, user.getId() + "; " +
                        user.getName() + "; " +
                        (String) user.getProperty("gender") + "; " +
                        (String) user.getProperty("email") + "; " +
                        user.getBirthday()+ "; " +
                        (String) user.getProperty("locale") + "; "
                        );*/


                        new Crear_Usuario().execute(datos);

                        onBackPressed();
                    }


                    //onBackPressed();

                } else {
                    // userName.setText("No estas logueado");
                }
            }
        });
    }
    else{

        onBackPressed();
    }


}
    //*****************************************************************************



    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {

                Log.d("Login SIC", "Facebook session opened");

            } else if (state.isClosed()) {

                Log.d("Login SIC", "Facebook session closed");
            }
        }
    };
    //*****************************************************************************





    public boolean checkPermissions() {
         s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }

    @Override
    public void onResume() {

        super.onResume();
        uiHelper.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

}