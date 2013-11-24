HOST: http://demoaap.apiary.io

--- Complex API IT. ---

---
This is a Complex App IT!
---

--
Resource: Tasty Meal
Truly tasty meal
### Properties
- `price[bigdecimal precision:10, scale:2]` - _Price_
- `name[string length:100, required]` - _Name_
--

--
Public Pub
Long description of Public Pub!
--

Find meal by name.
### Parameters
- `name [string]` - Name of Meal
GET pub/mealByName
> Accept: application/json
< 400
< Content-Type: application/json
Cannot find meal by name.

Update Meal in pub.
### Parameters
- `meal [meal]` - Meal for update
POST pub/updateMeal
> Accept: application/x-www-form-urlencoded
< 200
< Content-Type: application/json
Success update.
+++++
< 401
< Content-Type: application/json
Authorization violation.

Find all meals.
GET pub/allMeals
> Accept: application/json
< 200
< Content-Type: application/json
[{"name": "Hamburger", "price": 1000},{"name": "Chocolate", "price": 500}]


