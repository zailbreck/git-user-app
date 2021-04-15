package submission.dicoding.fundamental.gituser.ui.search


import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import submission.dicoding.fundamental.gituser.api.Resource
import submission.dicoding.fundamental.gituser.models.UserResponse
import submission.dicoding.fundamental.gituser.other.ConnectionChecker
import submission.dicoding.fundamental.gituser.other.Constants.Companion.CONVERSION_ERROR
import submission.dicoding.fundamental.gituser.other.Constants.Companion.NETWORK_FAILURE
import submission.dicoding.fundamental.gituser.other.Constants.Companion.NO_INTERNET_CONNECTION
import submission.dicoding.fundamental.gituser.repo.GetData
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getData: GetData, app: Application,
) : ConnectionChecker(app) {


    val searchUser: MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    private var searchUserResponse: UserResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    fun userSearch(username: String) = viewModelScope.launch {
        safeSearchUser(username)
    }

    init {
        userSearch("A")
    }

    private fun handleResponseSearchUser(response: Response<UserResponse>): Resource<UserResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (searchUserResponse == null || newSearchQuery != oldSearchQuery) {
                    oldSearchQuery = newSearchQuery
                    searchUserResponse = result
                }
                return Resource.Success(searchUserResponse ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeSearchUser(username: String) {
        newSearchQuery = username
        searchUser.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = getData.searchUsers(username)
                searchUser.postValue(handleResponseSearchUser(response))
            } else {
                searchUser.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (e: Throwable) {
            when (e) {
                is IOException -> searchUser.postValue(Resource.Error(NETWORK_FAILURE))
                else -> searchUser.postValue(Resource.Error(CONVERSION_ERROR))
            }
        }
    }


}