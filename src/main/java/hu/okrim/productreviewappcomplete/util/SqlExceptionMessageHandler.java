package hu.okrim.productreviewappcomplete.util;

public class SqlExceptionMessageHandler {
    public static String brandDeleteErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("fk_article_brand")) {
            errorMessage = "DELETION FAILED: This brand already has at least one article.";
        }
        return errorMessage;
    }
    public static String countryDeleteErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("fk_user_country")) {
            errorMessage = "DELETION FAILED: Country is referenced by at least one user.";
        }
        else if (errorMessage.contains("fk_brand_country")) {
            errorMessage = "DELETION FAILED: Country is referenced by at least one brand.";
        }
        else if (errorMessage.contains("fk_review_head_country")) {
            errorMessage = "DELETION FAILED: Country is referenced by at least one review.";
        }
        return errorMessage;
    }
    public static String categoryCreateErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("Category hierarchy can not exceed 3 in depth.")) {
            errorMessage = "CREATION FAILED: Category hierarchy can not exceed 3 in depth.";
        }
        if (errorMessage.contains("uq_category_name")) {
            errorMessage = "CREATION FAILED: This category name already exists. Duplicate names are not allowed.";
        }
        return errorMessage;
    }
    public static String categoryDeleteErrorMessage (Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("fk_category_category")) {
            errorMessage = "DELETION FAILED: This category has subcategories.";
        }
        else if (errorMessage.contains("fk_category_x_characteristic_category")) {
            errorMessage = "DELETION FAILED: This category has characteristics assigned already.";
        }
        else if (errorMessage.contains("fk_article_category")) {
            errorMessage = "DELETION FAILED: This category has articles.";
        }
        else if (errorMessage.contains("fk_aspect_category")) {
            errorMessage = "DELETION FAILED: This category has review aspects associated to it.";
        }
        return errorMessage;
    }
    public static String categoryUpdateErrorMessage(Exception ex){
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("characteristics are already assigned")) {
            errorMessage = "UPDATE FAILED: Changing this category is not allowed because characteristics are already assigned to it or it's subcategories, and updating would re-define the inheritance hierarchy.";
        }
        if (errorMessage.contains("products in this category were already rated")) {
            errorMessage = "UPDATE FAILED: Changing this category is not allowed because products in this category were already rated.";
        }
        return errorMessage;
    }
    public static String characteristicDeleteErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("fk_category_x_characteristic_characteristic")) {
            errorMessage = "DELETION FAILED: This characteristic is already assigned to at " +
                    "least one category.";
        }
        return errorMessage;
    }
    public static String characteristicCreateErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("Modifying characteristics that already describe a category is not allowed.")) {
            errorMessage = "UPDATE FAILED: Modifying characteristics that already describe a category is not allowed.";
        }
        return errorMessage;
    }
    public static String aspectUpdateErrorMessage(Exception ex){
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("Aspect already assigned")) {
            errorMessage = "UPDATE FAILED: Aspect with identical name already assigned to a category in the category hierarchy, which is not allowed.";
        }
        if(errorMessage.contains("This aspect cannot be modified because it already has reviews")) {
            errorMessage = "UPDATE FAILED: This aspect cannot be modified because there are already reviews that were made from this aspect.";
        }
        return errorMessage;
    }
    public static String aspectDeleteErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("This aspect cannot be deleted because it already has reviews")) {
            errorMessage = "DELETION FAILED: This aspect cannot be deleted because there are already reviews that were made from this aspect.";
        }
        return errorMessage;
    }
    public static String productDeleteErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("fk_review_head_product")) {
            errorMessage = "DELETION FAILED: This product cannot be deleted because it already has reviews.";
        }
        return errorMessage;
    }
    public static String packagingUpdateErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("Cannot change this packaging because it is used for at least one product.")) {
            errorMessage = "UPDATE FAILED: Cannot change this packaging because it is used for at least one product.";
        }
        return errorMessage;
    }
    public static String packagingDeleteErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("fk_product_packaging")) {
            errorMessage = "DELETION FAILED: This packaging is already assigned to a product.";
        }
        return errorMessage;
    }
    public static String articleDeleteErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("fk_product_article")) {
            errorMessage = "DELETION FAILED: This article already has products. Delete all products first!";
        }
        return errorMessage;
    }
    public static String articleUpdateErrorMessage(Exception ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("Cannot change article category")) {
            errorMessage = "UPDATE FAILED: Cannot change article category because this article already has products.";
        }
        return errorMessage;
    }
}
