// Generated code. Do not modify!
package com.mingnong.scanappnew.activity;

import com.zhy.m.permission.*;

public class LoginActivity$$PermissionProxy implements PermissionProxy<LoginActivity> {
@Override
 public void grant(LoginActivity source , int requestCode) {
switch(requestCode) {case 100:source.permissionSuccess();break;}  }
@Override
 public void denied(LoginActivity source , int requestCode) {
switch(requestCode) {case 100:source.permissionFail();break;}  }
@Override
 public void rationale(LoginActivity source , int requestCode) {
switch(requestCode) {}  }
@Override
 public boolean needShowRationale(int requestCode) {
switch(requestCode) {}
return false;  }

}
