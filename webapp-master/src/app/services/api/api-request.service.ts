import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpRequest,  HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable} from 'rxjs';
import 'rxjs/add/operator/catch';
import { UserInfoService, LoginInfoInStorage} from '../user-info.service';
import { AppConfig } from '../../app-config';
import { DebugContext } from '@angular/core/src/view';


@Injectable()
export class ApiRequestService {
    apiPath:string = this.appConfig.APIPath1;
    //url:string = "http://a00714cbfe34411e89c0602c6d493a4a-1344292358.us-east-1.elb.amazonaws.com:80"
    constructor(
        private appConfig:AppConfig,
        private http: HttpClient,
        private router:Router,
        private userInfoService:UserInfoService
    ) {}

    /**
     * This is a Global place to add all the request headers for every REST calls
     */
    getHeaders():HttpHeaders {
        // debugger;
        let headers = new HttpHeaders();
        let token = this.userInfoService.getStoredToken();
        headers = headers.set('Content-Type', 'application/json');
        if (token !== null) {
            headers = headers.append("Authorization", token);
        }
        return headers;
    }

    get(url:string, urlParams?:HttpParams):Observable<any>{       
        let me = this;
         if (url.includes("order")) {
             this.apiPath = this.appConfig.APIPath1;
         } else if (url.includes("product")) {
             this.apiPath = this.appConfig.APIPath2;
         } else if (url.includes("customer")) {
             this.apiPath = this.appConfig.APIPath3;
         } else if (url.includes("employee")) {
             this.apiPath = this.appConfig.APIPath4;
         }
         console.log(this.apiPath);        
        return this.http.get(this.apiPath + url)
            .catch(function(error:any){
                console.log("Some error in catch");
                if (error.status === 401 || error.status === 403){
                    me.router.navigate(['/logout']);
                }
                return Observable.throw(error || 'Server error')
            });
    }

    post(url:string, body:Object):Observable<any>{
        let me = this;
        return this.http.post(this.apiPath + url, JSON.stringify(body), { headers:this.getHeaders()})
            .catch(function(error:any){
                if (error.status === 401){
                    me.router.navigate(['/logout']);
                }
                return Observable.throw(error || 'Server error')
            });
    }

    put(url:string, body:Object):Observable<any>{
        let me = this;
        return this.http.put(this.apiPath + url, JSON.stringify(body), { headers:this.getHeaders()})
            .catch(function(error:any){
                if (error.status === 401){
                    me.router.navigate(['/logout']);
                }
                return Observable.throw(error || 'Server error')
            });
    }

    delete(url:string):Observable<any>{
        let me = this;
        return this.http.delete(this.apiPath + url, { headers:this.getHeaders()})
            .catch(function(error:any){
                if (error.status === 401){
                    me.router.navigate(['/logout']);
                }
                return Observable.throw(error || 'Server error')
            }); 
    }

}
