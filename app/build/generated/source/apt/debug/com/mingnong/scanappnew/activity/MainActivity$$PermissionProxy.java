// Generated code. Do not modify!
package com.mingnong.scanappnew.activity;

import com.zhy.m.permission.*;

public class MainActivity$$PermissionProxy implements PermissionProxy<MainActivity> {
@Override
 public void grant(MainActivity source , int requestCode) {
switch(requestCode) {case 100:source.permissionSuccess();break;}  }
@Override
 public void denied(MainActivity source , int requestCode) {
switch(requestCode) {case 100:source.permissionFail();break;}  }
@Override
 public void rationale(MainActivity source , int requestCode) {
switch(requestCode) {}  }
@Override
 public boolean needShowRationale(int requestCode) {
switch(requestCode) {}
return false;  }

}
