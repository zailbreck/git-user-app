package submission.dicoding.fundamental.gituser.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import submission.dicoding.fundamental.gituser.R
import submission.dicoding.fundamental.gituser.models.UserDetail
import submission.dicoding.fundamental.gituser.ui.detail.FollowersFollowingFragment
import submission.dicoding.fundamental.gituser.ui.detail.ReposFragment
import java.util.*

class ViewPagerAdapter
    (
    activity: AppCompatActivity,
    private val userDetail: UserDetail
) : FragmentStateAdapter(activity) {

    private val tabTitle = activity.resources.getStringArray(R.array.tab_title)

    override fun createFragment(pos: Int): Fragment {
        var fragment: Fragment? = null
        val title = tabTitle[pos].toLowerCase(Locale.ROOT)
        when (pos) {
            0 -> fragment = ReposFragment.setUpData(userDetail, title)
            1 -> fragment = FollowersFollowingFragment.setUpData(userDetail, title)
            2 -> fragment = FollowersFollowingFragment.setUpData(userDetail, title)
        }
        return fragment as Fragment
    }


    override fun getItemCount(): Int = tabTitle.size

}