package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("current_usd_exchange_rate")
    @Expose
    private String currentUsdExchangeRate;

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return The symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol The symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return The currentUsdExchangeRate
     */
    public String getCurrentUsdExchangeRate() {
        return currentUsdExchangeRate;
    }

    /**
     * @param currentUsdExchangeRate The current_usd_exchange_rate
     */
    public void setCurrentUsdExchangeRate(String currentUsdExchangeRate) {
        this.currentUsdExchangeRate = currentUsdExchangeRate;
    }
}
