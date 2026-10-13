import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
  palette: {
    mode: "light",
    primary: { main: "#2457c5" },
    success: { main: "#247448" },
    error: { main: "#b42318" },
    background: { default: "#f4f6fa", paper: "#ffffff" },
  },
  shape: { borderRadius: 14 },
  typography: {
    fontFamily: 'Inter, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif',
    h1: { fontSize: "clamp(1.8rem, 4vw, 2.25rem)", fontWeight: 750, letterSpacing: "-0.035em" },
    h2: { fontSize: "1.25rem", fontWeight: 700 },
    button: { fontWeight: 700, textTransform: "none" },
  },
  components: {
    MuiButton: { defaultProps: { disableElevation: true } },
    MuiCard: {
      styleOverrides: {
        root: { border: "1px solid #e4e8f0", boxShadow: "0 18px 48px rgba(24, 39, 75, 0.08)" },
      },
    },
  },
});
