import INITIAL_PAGINATION_STATE from "@/lib/constants/initialPagination";

const mapPaginationResponse = (apiResponse) => {
  if (!apiResponse) return INITIAL_PAGINATION_STATE;

  return {
    currentPage: apiResponse.number,
    pageSize: apiResponse.size,
    totalElements: apiResponse.totalElements,
    totalPages: apiResponse.totalPages,
    isFirst: apiResponse.first,
    isLast: apiResponse.last
  };
};

export default mapPaginationResponse;