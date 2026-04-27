package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.VacancyCard

class SearchViewModel : ViewModel() {

    private val state = MutableLiveData<SearchState>()
    open fun getState(): LiveData<SearchState> = state

    private var searchJob: Job? = null
    open fun scheduleSearch(searchTrack: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1000)
            doSearch(searchTrack)
        }
    }

    open fun doSearch(searchTrack: String) {

        val json: String = "[{\n" +
            "      \"id\": \"00080c3c-d8b5-3de1-b0ba-1ce1c46fdadd\",\n" +
            "      \"name\": \"ML-инженер\",\n" +
            "      \"company\": \"Facebook\",\n" +
            "      \"city\": \"Уфа\",\n" +
            "      \"salary\": {\n" +
            "        \"from\": 1000,\n" +
            "        \"to\": null,\n" +
            "        \"currency\": \"USD\"\n" +
            "      },\n" +
            "      \"logo\": \"https://upload.wikimedia.org/wikipedia/commons/5/51/Facebook_f_logo_%282019%29.svg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"0008f0fb-491f-3789-8de1-49cb412b1f2a\",\n" +
            "      \"name\": \"Frontend-разработчик\",\n" +
            "      \"company\": \"Google\",\n" +
            "      \"city\": \"Саратов\",\n" +
            "      \"salary\": {\n" +
            "        \"from\": 350000,\n" +
            "        \"to\": 500000,\n" +
            "        \"currency\": \"KZT\"\n" +
            "      },\n" +
            "      \"logo\": \"https://upload.wikimedia.org/wikipedia/commons/2/2f/Google_2015_logo.svg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"000941ae-88d6-371c-977b-d80f6384a77e\",\n" +
            "      \"name\": \"iOS-разработчик\",\n" +
            "      \"company\": \"Apple\",\n" +
            "      \"city\": \"Нижний Новгород\",\n" +
            "      \"salary\": {\n" +
            "        \"from\": 2500,\n" +
            "        \"to\": 4000,\n" +
            "        \"currency\": \"GEL\"\n" +
            "      },\n" +
            "      \"logo\": \"https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"00094fd1-a44c-3493-ba04-ff93efb94bc1\",\n" +
            "      \"name\": \"ML-инженер\",\n" +
            "      \"company\": \"IBM\",\n" +
            "      \"city\": \"Москва\",\n" +
            "      \"salary\": {\n" +
            "        \"from\": null,\n" +
            "        \"to\": 4000,\n" +
            "        \"currency\": \"GEL\"\n" +
            "      },\n" +
            "      \"logo\": \"https://upload.wikimedia.org/wikipedia/commons/5/51/IBM_logo.svg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"000a0d90-d62c-3327-8608-5bf69aa596c6\",\n" +
            "      \"name\": \"Backend-разработчик\",\n" +
            "      \"company\": \"Adobe\",\n" +
            "      \"city\": \"Тольятти\",\n" +
            "      \"salary\": {\n" +
            "        \"from\": null,\n" +
            "        \"to\": 4000,\n" +
            "        \"currency\": \"GEL\"\n" +
            "      },\n" +
            "      \"logo\": \"https://upload.wikimedia.org/wikipedia/commons/6/6e/Adobe_Corporate_logo.svg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"000a1715-fda5-301a-89a8-5e0e5c2fa5ef\",\n" +
            "      \"name\": \"Продуктовый менеджер\",\n" +
            "      \"company\": \"Microsoft\",\n" +
            "      \"city\": \"Барнаул\",\n" +
            "      \"salary\": {\n" +
            "        \"from\": null,\n" +
            "        \"to\": null,\n" +
            "        \"currency\": \"RUB\"\n" +
            "      },\n" +
            "      \"logo\": \"https://upload.wikimedia.org/wikipedia/commons/4/44/Microsoft_logo.svg\"\n" +
            "    }\n]"



        val type = object : TypeToken<List<VacancyCard>>() {}.type
        val mockData = Gson().fromJson<List<VacancyCard>>(json, type)
/*
        val mockData = listOf(
            VacancyCard(
                id = "a1715-fda5-301a-89a8-5e0e5c2fa5ef",
                name = "Продуктовый менеджер",
                company = "Microsoft",
                city = "Барнаул",
                salary = Salary (
                    from = null,
                    to = 300000,
                    currency = "RUB"
                ),
                logo = "https://upload.wikimedia.org/wikipedia/commons/4/44/Microsoft_logo.svg"
            ),
        )
*/
        val content = SearchState.Content(mockData)
        state.postValue(content)

//        performedSearchStr = searchTrack
//        searchInProgress = true
//
//        searchDebounce(searchTrack)
    }


}
