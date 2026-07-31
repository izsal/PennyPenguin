# Implementation Plan - Category Management and Reports

The goal is to add functionality to create custom categories and a dedicated screen for category-based reports.

## Proposed Changes

### Data & Domain Layer

#### [NEW] [CategoryEntity.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/local/CategoryEntity.kt)
- Define Room entity for `Category`.

#### [NEW] [CategoryDao.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/local/CategoryDao.kt)
- Define DAO methods for CRUD operations on categories.

#### [MODIFY] [AppDatabase.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/local/AppDatabase.kt)
- Register `CategoryEntity` and add `categoryDao`.

#### [NEW] [CategoryRepository.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/domain/repository/CategoryRepository.kt)
- Define interface for category operations.

#### [NEW] [CategoryRepositoryImpl.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/repository/CategoryRepositoryImpl.kt)
- Implement `CategoryRepository`.

#### [MODIFY] [TransactionDao.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/local/TransactionDao.kt)
- Add query to get category-wise summaries (e.g., total amount per category).

### Presentation Layer

#### [MODIFY] [Screen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/navigation/Screen.kt)
- Add `AddCategory` and `CategoryReports` screens.

#### [NEW] [AddCategoryScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/categories/AddCategoryScreen.kt)
- UI for creating a new category (Name, Icon, Type).

#### [NEW] [CategoryReportsScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/reports/CategoryReportsScreen.kt)
- UI to display reports filtered by category (list of categories with total spending/income).

#### [MODIFY] [MainScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/MainScreen.kt)
- Update navigation graph to include new screens.

## Verification Plan

### Automated Tests
- Unit tests for `CategoryDao` and `CategoryRepository`.
- Verify `TransactionDao` query for category summaries.

### Manual Verification
- Navigate to "Add Category" and create a new category.
- Verify the new category appears in the transaction creation screen.
- Navigate to "Category Reports" and verify the summaries match the transactions.
