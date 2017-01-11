/**
 * Created by Peng on 2016-09-19.
 */
(function(){
    var app = angular.module("petStore", []);
    app.controller('myController', ['$scope', '$http', '$compile', '$timeout', '$element', function(scope, http, compile, timeout, element) {

            scope.closeReminder = function() {scope.reminder = false;};
            scope.closeRestfullError = function () { scope.jsonError = ""};
            scope.closeRestfullMessage = function () {scope.jsonMessage = ""};
            scope.addorCancel = 'Add';
            scope.photoCount = 1;
            scope.showSearching = false;
            scope.overlayShow = false;
            scope.preloadShow = false;
            scope.viewDetailShow = false;
            scope.viewPhotos = [];
            scope.viewPet = function (id) {
                scope.overlayShow = true;
                scope.preloadShow = true;
                scope.viewDetailShow = false;
                var response = http.get("/pet/"+id);
                response.success(function (pet) {
                    if (pet.id) {
                        scope.preloadShow = false;
                        scope.viewName = pet.name;
                        scope.viewStatus = pet.status ? "Available" : "Unavailable"
                        scope.viewCategory = pet.category.name
                        scope.viewTags = pet.tags
                        scope.viewPhotos = pet.photoUrls;
                        scope.viewDetailShow = true;
                    }
                    else {
                        scope.jsonError = pet.message;
                        scope.overlayShow = false;
                        scope.preloadShow = false;
                    }
                })
                response.error(function(obj){
                    scope.jsonError = obj.message;
                    scope.overlayShow = false;
                    scope.preloadShow = false;
                })
            };



            scope.deletePet = function (id) {
                var response = http.delete("/pet/"+id);
                response.success(function (model) {
                    if (model.msg != 'ok') {
                        scope.jsonError = model.msg;
                        return;
                    }
                    for (var i=0; i < scope.pets.length; i++){
                        if (scope.pets[i].id==id) {
                            scope.pets.splice(i,1);
                            break;
                        }
                    }
                    scope.jsonMessage = "Deleted successfully!!!"
                })
                response.error(function(obj){
                    scope.jsonError = obj.message;
                })
            };
//        scope.showResults = false;
            scope.focuseditem = -1;
            scope.setFocusedItem = function (id) {
                scope.focuseditem = id;
            }
            scope.lostFocus = function () {
                if (scope.focuseditem < 0) {
                    scope.showResults = false
                }
            }

            scope.runLink = function (id, name) {
                var currentTags = scope.tagKeyword.split(",");
                currentTags[currentTags.length - 1] = name + ",";
                scope.tagKeyword = currentTags.join(",")
                scope.searchResultTags = [];
                angular.element(document.getElementById('tagKeyword').focus());
            };

            scope.SearchTags = function () {
                if (!scope.tagKeyword) {
                    scope.searchResultTags = [];
                    return;
                }
                scope.showSearching = true;
//          alert(scope.tagKeyword.replace(/;/g, "%3b"))
                var response = http.get("/tags/"+scope.tagKeyword.replace(/;/g, "%3b"))
                response.success(function (json) {
//            scope.showResults = true;
                    scope.showSearching = false;
                    scope.searchResultTags = json;
                })
            };

            scope.addMorePic =  function(){
                angular.element(document.getElementById('photosDiv')).append("<input type='file' class='form-control' name='photos'" + " id='photo"+ (++scope.photoCount) + "' accept='image/*'/>");
            };

            scope.clearUpAllFields = function () {
                scope.petName = '';
                scope.categoryModel = ''
                scope.tagKeyword = ''
                while(scope.photoCount > 1) {
                    angular.element(document.getElementById('photosDiv').removeChild(document.getElementById('photo'+(scope.photoCount--))))
                }
            }
            scope.hideOverlay = function () {
                scope.overlayShow = false;
            };
            scope.switchAddorCancel = function() {
                if (scope.addorCancel=='Add') {
                    scope.addorCancel='Cancel'
                    scope.clearUpAllFields();
                    var response = http.get("/allCategories")
                    response.success(function (json) {
                        scope.categories = json;
                    })
                    var response1 = http.get("/allTags")
                    response1.success(function (json) {
                        scope.tags = json;
                    })
                    scope.addPetForm = true
                }
                else {
                    scope.addorCancel='Add'
                    scope.addPetForm = false;
                }
            };

            scope.addPet = function () {

            };

            var response = http.get("/allPets")
            response.success(function (json) {
                scope.pets = json;
                scope.preloadListShow = false;
            })
            response.error(function(json){
                alert(json);
            })

        }]);

    app.controller('testController', ['$scope', function(scope){
        scope.mytest = function() {
            alert(1);
        }
        this.mytest1 = function() {
            alert(2);
        }
    }]);

    app.directive('petDetailOverlay', function(){
        return {
            restrict: 'E',
            templateUrl: 'pet-detail-overlay.html'
        };
    });
    app.directive('tagSearchTypeahead', function(){
        return {
            retstirct: 'A',
            templateUrl: 'tag-search-typeahead.html'
        }
    });
    app.directive('tooltip', function($compile) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var icon = '';
                if (attrs.tooltipicon != '') {
                    icon = '<i class="'+attrs.tooltipicon+'"></i>';
                }
                else {
                    icon = '<i class="fa fa-question-circle"></i>';
                }
                element.html('<span class="mytooltip"><span class="link">' + attrs.tooltiplabel + icon + '</span><span class="tooltip-content">' + attrs.tooltip + '</span></span>');
                element.hover(function(){
                    $(this).find('.tooltip-content').css({'display':'block'});
                },function(){
                    $(this).find('.tooltip-content').css({'display':'none'});
                });
            }
        };
    });
})();