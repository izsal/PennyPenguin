# Walkthrough - Category Edit and Delete Functionality

I have successfully added the ability to edit and delete custom categories from the management screen.

## Changes Made

### Presentation Layer
- **Edit Support**: Updated `AddCategoryViewModel` and `AddCategoryScreen` to handle existing categories. If you open a category for editing, the title changes to "Edit Category" and the fields are pre-filled.
- **Category List UI**: Added an **Edit** icon (pencil) to each custom category in the list.
- **Delete Support**: Improved the **Delete** icon button (trash can) to be more prominent for custom categories.
- **Navigation**: Updated the navigation graph to support passing `categoryId` as an optional argument to the Add/Edit Category screen.

## How to Test

1. **Edit a Category**:
   - Go to **Profile > Categories**.
   - Tap the **Pencil icon** (Edit) on a custom category you've created.
   - Change its name or icon.
   - Tap **Update Category**.
   - Verify the changes in the list.
2. **Delete a Category**:
   - In the **Categories** list, tap the **Trash icon** (Delete) on a custom category.
   - Verify it disappears from the list.
   - Check the transaction picker to ensure it's also removed there.

> [!NOTE]
> Default categories (like Food, Salary, etc.) cannot be edited or deleted as they are part of the core application.
