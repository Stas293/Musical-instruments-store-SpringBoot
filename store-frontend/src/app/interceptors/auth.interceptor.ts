import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpInterceptorFn} from '@angular/common/http';
import {Observable} from 'rxjs';

export const authInterceptor : HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('jwtToken');
  const request = req.clone({
    setHeaders: {
      ...(token ? {Authorization: `${token}`} : {}),
    },
  });
  return next(request);
}
