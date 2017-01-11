<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script src="list.js"></script>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
  <link rel='stylesheet' href='css/bootstrap.min.css'>
  <link rel="stylesheet" type="text/css" href="/css/main.css">
  <link rel="stylesheet" type="text/css" href="/css/tooltip.css">
  <link rel="stylesheet" type="text/css" href="/font-awesome-4.7.0/css/font-awesome.min.css">
</head>
<body ng-app="petStore" class="container-fluid" >

<div id="test" ng-controller="myController" class="col-lg-6 col-md-offset-3">

  <div><h1 class="text-center">Pet Store</h1></div>

  <span>I am ${user}</span>

  <span data-ng-show="'${user}'=='admin'" data-tooltip="Admin user has full authentication." data-tooltiplabel="" data-tooltipicon="fa fa-question-circle"></span>
  <span data-ng-show="'${user}'=='user'" data-tooltip="Regular user cannot execute Delete operation!!" data-tooltiplabel="" data-tooltipicon="fa fa-question-circle"></span>


  <a href="logout"><span style="text-decoration: underline;">Logout</span></a>
  <div ng-click="closeReminder()" ng-show="reminder" ng-init="reminder=true" ng-cloak>
    <div class="border: 1px solid black;" >
      <h3 class="bg-success" ng-show="${message!=null}">${message}</h3>
      <h3 class="bg-warning" ng-show="${error!=null}">${error}</h3>
    </div>
  </div>
  <div ng-include="'jsonMessage.html'"></div>

  <div>
    <table class="table">
      <tbody>
        <tr >
          <th style="width: 10%;">Id</th>
          <th style="width: 30%;">Name</th>
          <th style="width: 20%;">Status</th>
          <th style="width: 20%;">Category</th>
          <th style="width: 5%;"/>
          <th style="width: 5%;"/>
        </tr>

        <tr ng-class = "{'active': $index % 2}" ng-repeat="pet in pets" ng-cloak>
          <td>{{pet.id}}</td>
          <td>{{pet.name}}</td>
          <td>{{pet.status?"Available":"Unavailable"}}</td>
          <td>{{pet.category.name}}</td>
          <td><a href="javascript:void(0)" ng-click="deletePet(pet.id)">Delete</a></td>
          <td><a href="javascript:void(0)" ng-click="viewPet(pet.id)">View</a></td>
        </tr>
      </tbody>
    </table>
  </div>

  <div style="text-align:center;height:100%" ng-show="preloadListShow" ng-init="preloadListShow=true">
    <div style="height:20%"></div>
    <div style="height:80%"><img src="images/preloader.gif"/></div>
  </div>

  <div>
    <button ng-style="{width: '100%', 'margin-top':'10px', 'margin-bottom':'10px','border':'solid 2px grey', 'border-radius': '25px', 'background-color':'lightgrey'}"
            ng-click="switchAddorCancel()">
      {{addorCancel}}
    </button>
  </div>

  <div ng-show="addPetForm" ng-init="addPetForm=false" ng-cloak>
    <form name="addPet" action="pet" method="post" enctype="multipart/form-data">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="form-group">
        <label for="name">Pet Name</label>
        <input type="text" class="form-control" name="name" id="name" ng-model="petName" placeholder="Your Pet Name" autocomplete="off" required>
      </div>

      <div class="form-group">
        <label for="category">Category</label>
          <select name="category" id="category" class="form-control" ng-model="categoryModel" required>
            <option ng-repeat="category in categories" value={{category.id}}>{{category.name}}</option>
            <option value=0>Custom</option>
          </select>
          <input type="text" ng-disabled="categoryModel != 0" class="form-control" name="newCategoryName"
                 placeholder="Custom Category Name" ng-required="categoryModel == 0" ng-model="customCategory"/>
      </div>

      <div class="form-group">
        <label>Upload Photos</label>
        <div id="photosDiv">
          <input type='file' class='form-control' name='photos' accept='image/*' multiple/>
        </div>
        <button type="button" class="btn btn-info btn-sm" ng-click="addMorePic()">More</button>
      </div>

      <div class="form-group" tag-search-typeahead></div>
	  
      <br>
      <br>
      <button type="submit" style="float:right" ng-class="{'disabled': addPet.$invalid, 'btn': true, 'btn-success': true}">Submit</button>
    </form>
  </div>


  <pet-detail-overlay ng-cloak ng-show="overlayShow" ng-click="hideOverlay()"></pet-detail-overlay>

</div>

</body>
</html>