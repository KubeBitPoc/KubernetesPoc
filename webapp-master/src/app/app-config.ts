import { Injectable } from '@angular/core';

/**
 * This is a singleton class
 */
@Injectable()
export class AppConfig { 
    //Provide all the Application Configs here

    public version: string = "1.0.0";
    public locale: string = "en-US";
    public currencyFormat = { style: "currency", currency: "USD" };
    public dateFormat = { year: 'numeric', month: 'short', day: 'numeric' };

    // API Related configs
    public apiPort1: string;
    public apiPort2: string;
    public apiPort3: string;
    public apiPort4: string;
    public apiProtocol: string;
    public apiHostName: string;
    // public baseApiPath: string;
    public url: string;
    public baseApiPath1: string;
    public baseApiPath2: string;
    public baseApiPath3: string;
    public baseApiPath4: string;
    public APIPath1: string;
    public APIPath2: string;
    public APIPath3: string;
    public APIPath4: string;

    constructor() {
        // if (this.apiProtocol===undefined){
        //     this.apiProtocol = window.location.protocol;
        // }
        // if (this.apiHostName===undefined){
        //     this.apiHostName = window.location.hostname;
        // }
        // if (this.apiPort===undefined){
        //     this.apiPort = window.location.port;
        // }
        // if (this.apiHostName.includes("infomud") || this.apiHostName.includes("heroku")){
        //     this.baseApiPath = this.apiProtocol + "//" + this.apiHostName + "/";
        // }
        // else{
        //     this.baseApiPath = this.apiProtocol + "//" + this.apiHostName + ":" + this.apiPort +"/";
        // }
        // if (this.locale===undefined){
        //     this.locale = navigator.language;
        //
        // }
        
        //services name - api-service-one.default.local
        //service name2

        this.url = window.location.origin;
        this.url = this.url + ":";
        this.baseApiPath1 = this.url;
        this.baseApiPath2 = this.url;
        this.baseApiPath3 = this.url;
        this.baseApiPath4 = this.url;
        
        this.apiPort1 = "80";
        this.apiPort2 = "80";
        this.apiPort3 = "80";
        this.apiPort4 = "80";
        
        [
            this.APIPath1, this.APIPath2, this.APIPath3, this.APIPath4
        ] = [
                this.baseApiPath1 + this.apiPort1 + "/", this.baseApiPath2 + this.apiPort2 + "/", this.baseApiPath3 + this.apiPort3 + "/", this.baseApiPath4 + this.apiPort4 + "/"
            ]
    }
}
