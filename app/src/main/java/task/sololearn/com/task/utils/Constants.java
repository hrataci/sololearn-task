package task.sololearn.com.task.utils;

public interface Constants {
    interface Connection {
        String BASE_URl = "https://content.guardianapis.com/search";
        String API_KEY = "a2b4f1b4-cd43-48fc-ae28-ffc2b96dcafd";
        String PARAMS = "&show-fields=headline,standfirst,trailText,firstPublicationDate,shortUrl,thumbnail,bodyText";
        String URL = BASE_URl + "?api-key=" + API_KEY + PARAMS;
        String PAGE = "&page=";
    }

    interface Preference {
        String SHARED = "sololearn.task";
        String IS_BACKGROUNDED = "is_backgrounded";
    }

    interface Timer {
        int SECONDS = 30;
    }

    interface JsonData {
        String RESPONSE = "response";
        String STATUS = "status";
        String STATUS_OK = "ok";
        int PAGE_SIZE = 10;
        String CURRENT_PAGE = "currentPage";
        String RESULTS = "results";
    }

    interface Date {
        String PATTERN = "yyyy-MM-dd'T'hh:mm:ss'Z'";
    }

    interface ViewType {
        int PINNED = 1;
        int NEWS = 2;
    }
    interface  Intent{
        String ID = "id";
    }
}
