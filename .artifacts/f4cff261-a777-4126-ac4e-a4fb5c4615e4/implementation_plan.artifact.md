# Implementation Plan - Edit and Delete Categories

The goal is to complete the CRUD operations for custom categories by adding an edit feature and ensuring delete functionality is easily accessible.

## Proposed Changes

### Presentation Layer

#### [MODIFY] [Screen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/navigation/Screen.kt)
- Update `AddCategory` route to accept an optional `categoryId` parameter: `add_category?categoryId={categoryId}`.

#### [MODIFY] [MainScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/MainScreen.kt)
- Update the `composable` for `AddCategory` to retrieve the `categoryId` argument and pass it to the screen/ViewModel.

#### [MODIFY] [AddCategoryViewModel.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/categories/AddCategoryViewModel.kt)
- Add logic to load an existing category if `categoryId` is provided.
- Update `saveCategory` to update the existing category instead of always creating a new one (using the same ID).

#### [MODIFY] [CategoryListScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/categories/CategoryListScreen.kt)
- Add an "Edit" icon button next to the delete button for custom categories.
- Ensure clicking the edit button navigates to `AddCategory` with the correct ID.

## Verification Plan

### Manual Verification
1. Navigate to **Profile > Categories**.
2. Create a custom category.
3. Click the **Edit** icon on the new category.
4. Change the name/icon and save.
5. Verify the category is updated in the list.
6. Click the **Delete** icon and verify the category is removed.
7. Go to the transaction screen and verify the changes are reflected in the picker.
