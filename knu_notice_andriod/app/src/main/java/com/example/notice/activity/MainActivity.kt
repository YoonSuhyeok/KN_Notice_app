package com.example.notice.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.notice.*
import com.example.notice.Jsoup.SoupClient
import com.example.notice.Recyclerviews.FilterAdapter
import com.example.notice.Recyclerviews.NoticeAdapter
import com.example.notice.Recyclerviews.RecyclerDecoration
import com.example.notice.api.RetrofitClient
import com.example.notice.api.oauth
import com.example.notice.api.request.validateRequest
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.Notice
import com.example.notice.defaultClass.NoticeNameId
import com.example.notice.dialog.CustomDialog
import com.example.notice.dialog.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.Nullable

class MainActivity : AppCompatActivity() {

    private var filters: ArrayList<NoticeNameId> = arrayListOf()
    private var noticeList: List<Notice> = arrayListOf()
    private var allList: List<String> = arrayListOf()
    private var selectedIds: ArrayList<Int> = arrayListOf()
    private var noticeAdapter: NoticeAdapter = NoticeAdapter(true, this, noticeList, filters)
    private var filterAdapter: FilterAdapter = FilterAdapter(filters, noticeAdapter)
    //Initialize Loader
    private val loadingDialogFragment by lazy { LoadingDialog() }
    private var isBrowser: Boolean = false
    companion object {
        var position = 0
    }

    private final val RC_GET_TOKEN = 9002
    private lateinit var mGoogleSignInClient: GoogleSignInClient;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getBooleanExtra("UpdateFilter", false)) overridePendingTransition(
            R.anim.center_to_right,
            R.anim.none
        )
        else overridePendingTransition(R.anim.horizon_enter, R.anim.none)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()

        recyclerview.setHasFixedSize(true)
        recyclerview.addItemDecoration(RecyclerDecoration(0))
        recyclerview.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        filterRecycle.setHasFixedSize(true)
        filterRecycle.addItemDecoration(RecyclerDecoration(0))
        filterRecycle.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

        var shared = getSharedPreferences("updateDate", 0)
        val edit: SharedPreferences.Editor = shared.edit()
        val f = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
        val beforeTime = shared.getString("lastUpdate", null)

        val lastIds = shared.getString("lastIds", "")

        shared = getSharedPreferences("major", 0)
        val mutSet: MutableSet<String> = shared.all.keys
        val saveIds = StringBuffer("")
        allList = ArrayList(mutSet)

        for (sel in allList) {
            if (shared.all[sel] == true) {
                saveIds.append(db.majorDao().getMId(sel))
            }
        }

        if (beforeTime == null) {
            println("날짜 초기 저장")
            fetchExp(db)
            edit.putString("lastIds", saveIds.toString())
            edit.putString("lastUpdate", f.format(Date()).toString())
            edit.apply()
        } else {
            val beforeDate: Date = f.parse(beforeTime)
            val now: Date = f.parse(f.format(Date()))
            val diff = (now.time - beforeDate.time) / (1000 * 60 * 60)
            if (diff >= 1 || lastIds != saveIds.toString()) {
                println("패치 재실행")
                fetchExp(db)
                edit.putString("lastIds", saveIds.toString())
                edit.putString("lastUpdate", f.format(Date()).toString())
                edit.commit()
            }
        }

        filBtn.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }
        shared = getSharedPreferences("etcSetValues", 0)
        isBrowser = shared.getBoolean("isBrowser", true)
        fetchAdapter()

        // GOOGLE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun getIdToken() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GET_TOKEN)
    }

    private fun refreshIdToken() {
        // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
        // already has a valid token this method may complete immediately.
        //
        // If the user has not previously signed in on this device or the sign-in has expired,
        // this asynchronous branch will attempt to sign in the user silently and get a valid
        // ID token. Cross-device single sign on will occur in this branch.

        mGoogleSignInClient.silentSignIn()
            .addOnCompleteListener(
                this
            ) { task -> handleSignInResult(task) }
    }

    // [START handle_sign_in_result]
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken

            if( idToken != null ) {
                val requestDTO = validateRequest(idToken = idToken)

                val client = RetrofitClient.getClient()?.create(oauth::class.java)
                val temp = client?.validate(requestDTO)?.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        TODO("Not yet implemented")
                        Log.w("resonse", response.toString())
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.w("resonse", t)
                    }
                })

            }

            // TODO(developer): send ID Token to server and validate
            updateUI(account)
        } catch (e: ApiException) {
            Log.w("TAG", "handleSignInResult:error", e)
            updateUI(null)
        }
    }
    // [END handle_sign_in_result]

    private fun signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(
            this
        ) { updateUI(null) }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(
            this
        ) { updateUI(null) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GET_TOKEN) {
            // [START get_id_token]
            // This task is always completed immediately, there is no need to attach an
            // asynchronous listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            // [END get_id_token]
        }
    }

    private fun updateUI(@Nullable account: GoogleSignInAccount?) {
        if (account != null) {
//            (findViewById<View>(R.id.status) as TextView).setText(R.string.signed_in)
            val idToken = account.idToken
            //mIdTokenTextView.setText(getString(R.string.id_token_fmt, idToken))
//            findViewById<View>(R.id.sign_in_button).visibility = View.GONE
//            findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
//            mRefreshButton.setVisibility(View.VISIBLE)
        } else {
//            (findViewById<View>(R.id.status) as TextView).setText(R.string.signed_out)
//            mIdTokenTextView.setText(getString(R.string.id_token_fmt, "null"))
//            findViewById<View>(R.id.sign_in_button).visibility = View.VISIBLE
//            findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.GONE
//            mRefreshButton.setVisibility(View.GONE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val shared = getSharedPreferences("etcSetValues", 0)

        if (menu != null) {
            menu.getItem(4).isChecked = shared.getBoolean("isBrowser", true)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.login -> {
                getIdToken()
                true
            }
            R.id.developInfo -> {
                startActivity(Intent(this, DevelopInfoActivity::class.java))
                true
            }
            R.id.question -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScb-WfexuBzW8dnyYME1L_5maBrtozwQT6XdCXp9iTez-Z4og/viewform?usp=pp_url")
                    )
                )
                true
            }
            R.id.bookmark -> {
                startActivity(Intent(this, BookmarkActivity::class.java))
                true
            }
            R.id.switchBtn -> {
                val dialog = CustomDialog.CustomDialogBuilder()
                    .setTitle("테마", this)
                    .create()
                dialog.show(supportFragmentManager, dialog.tag)
                true
            }
            R.id.browserKind -> {
                val shared = getSharedPreferences("etcSetValues", 0)
                val edit: SharedPreferences.Editor = shared.edit()
                item.isChecked = shared.getBoolean("isBrowser", true)
                item.isChecked = !item.isChecked

                edit.putBoolean("isBrowser", item.isChecked)

                // 꺼졌다 켜져도 변경되어있나 궁금.
                noticeAdapter.changeIsBrowser(item.isChecked)
                edit.apply()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun searchList(view: View) {
        noticeAdapter.filter.filter(search.text)
    }

    private fun fetchAdapter() {
        val db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
        val shared: SharedPreferences = getSharedPreferences("major", 0)
        val mutSet: MutableSet<String> = shared.all.keys
        selectedIds = arrayListOf()
        allList = ArrayList(mutSet)
        val filters = ArrayList<NoticeNameId>()
        
        println("시작")
        for (sel in allList) {
            if (shared.all[sel] == true) {
                println(sel)
                val num = db.majorDao().getMId(sel)
                filters.add(NoticeNameId(sel, arrayListOf(num), false))
                selectedIds.add(num)
            }
        }
        filters.add(0, NoticeNameId("전체", selectedIds, true))

        noticeList = db.noticeDao().getFil(selectedIds)

        db.close()

        noticeAdapter = NoticeAdapter(isBrowser, this, noticeList, filters)

        filterAdapter = FilterAdapter(filters, noticeAdapter)

        filterRecycle.adapter = filterAdapter
        recyclerview.adapter = noticeAdapter

        noticeAdapter.filter.filter("")
    }

    private fun fetchExp(db: AppDataBase) {
        val shared: SharedPreferences = getSharedPreferences("major", 0)
        val mutSet: MutableSet<String> = shared.all.keys

        selectedIds = arrayListOf()
        allList = ArrayList(mutSet)
        val client = SoupClient(db)

        for (x in allList) if (shared.all[x] == true){
            client.fetchInfoList[db.majorDao().getMId(x) - 1].isSelect = true
        }

        var count:Long = 0
        for (x in client.fetchInfoList) {
            if (x.isSelect) {
                count++
                client.fetchData(x.index, x.baseUrl, x.cutBaseUrlNumber, x.cutFetchUrlNumber)
            }
        }
        // show Loader
        showProgressDialog()
        CoroutineScope(Main).launch {
            delay(count * 1300)
            fetchAdapter()
            //Hide Loader
            hideProgressDialog()
            noticeAdapter.notifyDataSetChanged()

        }
    }

    private fun showProgressDialog() {
        if (!loadingDialogFragment.isAdded) {
            loadingDialogFragment.show(supportFragmentManager, "loader")
        }
    }

    private fun hideProgressDialog() {
        if (loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismissAllowingStateLoss()
        }
    }
}