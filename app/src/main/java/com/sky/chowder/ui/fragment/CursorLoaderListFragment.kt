package com.sky.chowder.ui.fragment

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.support.v4.app.ListFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.widget.SimpleCursorAdapter
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import com.sky.SkyApp

class CursorLoaderListFragment : ListFragment(), SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    private var mAdapter: SimpleCursorAdapter? = null

    private var curFilter: String? = null//当前筛选的条件

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setEmptyText("No phone numbers")
        setHasOptionsMenu(true)
        mAdapter = SimpleCursorAdapter(activity,
                android.R.layout.simple_list_item_2, null,
                arrayOf(Phone.DISPLAY_NAME, Phone.NUMBER),
                intArrayOf(android.R.id.text1, android.R.id.text2), 0)
        listAdapter = mAdapter
        loaderManager.initLoader(0, null, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val item = menu!!.add("Search")
        item.setIcon(android.R.drawable.ic_menu_search)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        val search = SearchView(activity)//标题搜索栏
        search.setOnQueryTextListener(this)
        item.actionView = search
    }

    override fun onQueryTextChange(newText: String): Boolean {
        curFilter = if (!TextUtils.isEmpty(newText)) newText else null
        loaderManager.restartLoader(0, null, this)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean = true

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        SkyApp.getInstance().showToast("Item clicked: " + id)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
        val baseUri: Uri = if (curFilter != null)
            Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(curFilter))
        else Phone.CONTENT_URI

        val select = "((" + Phone.DISPLAY_NAME + " NOTNULL) AND (" + Phone.HAS_PHONE_NUMBER + "=1) AND (" + Phone.DISPLAY_NAME + " != '' ))"
        return CursorLoader(activity, baseUri,
                CONTACTS_SUMMARY_PROJECTION, select, null,
                Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter?.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter?.swapCursor(null)
    }

    companion object {
        internal val CONTACTS_SUMMARY_PROJECTION = arrayOf(
                Phone._ID,
                Phone.DISPLAY_NAME,
                Phone.CONTACT_STATUS,
                Phone.CONTACT_PRESENCE,
                Phone.PHOTO_ID,
                Phone.LOOKUP_KEY,
                Phone.NUMBER)
    }
}