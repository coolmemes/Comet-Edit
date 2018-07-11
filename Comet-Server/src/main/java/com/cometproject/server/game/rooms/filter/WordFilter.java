package com.cometproject.server.game.rooms.filter;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.storage.queries.filter.FilterDao;
import com.cometproject.server.utilities.FilterUtil;
import org.apache.log4j.Logger;

import java.util.Map;


public class WordFilter {
    private Map<String, String> wordfilter;

    public WordFilter() {
        this.loadFilter();
    }

    public void loadFilter() {
        if (this.wordfilter != null) {
            this.wordfilter.clear();
        }

        this.wordfilter = FilterDao.loadWordfilter();

        Logger.getLogger(WordFilter.class.getName()).info("Loaded " + wordfilter.size() + " filtered words");
    }

    public FilterResult filter(String message) {
        String filteredMessage = message;

        message = FilterUtil.process(message.toLowerCase());


        for (Map.Entry<String, String> word : wordfilter.entrySet()) {
            if (message.toLowerCase().contains(word.getKey()))
                return new FilterResult(true, word.getKey());
                filteredMessage = filteredMessage.replace("(?i)" + word.getKey(), word.getValue());
            }

        return new FilterResult(filteredMessage, !message.equals(filteredMessage));
    }

    public void addWord(String word) {
        FilterDao.insert(word);
        Logger.getLogger(WordFilter.class.getName()).info("Loaded " + wordfilter.size() + " filtered words");
    }

    public void removeWord(String word) {
        FilterDao.remove(word);
        Logger.getLogger(WordFilter.class.getName()).info("Loaded " + wordfilter.size() + " filtered words");
    }

    public void save() {
        FilterDao.save(this.wordfilter);
    }
}
