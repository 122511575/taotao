package com.taotao.search.service;

import com.taotao.pojo.SearchResult;
import com.taotao.pojo.TaotaoResult;

public interface SearchService {
    TaotaoResult importSolr();

    SearchResult findItemSearch(String query, Integer page);
}
