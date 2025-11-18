import NotFoundOrRedirect from '../pages/NotFoundOrRedirect.jsx';

export const notfoundRoutes = [
  {
    path: '*',
    element: <NotFoundOrRedirect />,
  },
];

