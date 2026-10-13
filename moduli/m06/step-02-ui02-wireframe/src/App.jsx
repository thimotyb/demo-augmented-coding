import { useState } from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Divider,
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
  Stack,
  Typography,
} from "@mui/material";

const scenarios = {
  approved: { label: "Approvazione al primo tentativo", result: "approved" },
  recovered: { label: "Errore transitorio, poi approvazione", result: "approved", attempts: 2 },
  exhausted: { label: "Tre errori transitori", result: "exhausted", attempts: 3 },
};

export default function App() {
  const [scenario, setScenario] = useState("approved");
  const [status, setStatus] = useState("ready");

  function submitPayment() {
    if (status === "pending") return;
    setStatus("pending");
    window.setTimeout(() => setStatus(scenarios[scenario].result), 1400);
  }

  function selectScenario(event) {
    setScenario(event.target.value);
    setStatus("ready");
  }

  function resetDemo() {
    setStatus("ready");
  }

  return (
    <Box component="main" sx={{ minHeight: "100vh", px: 2, py: { xs: 5, sm: 8 } }}>
      <Box sx={{ width: "min(100%, 480px)", mx: "auto" }}>
        <Stack spacing={1} sx={{ mb: 3, textAlign: "center" }}>
          <Typography variant="overline" color="primary.main" fontWeight={750}>
            CHECKOUT · UI-02
          </Typography>
          <Typography component="h1" variant="h1">Conferma il pagamento</Typography>
          <Typography color="text.secondary">
            Controlla l’importo prima di confermare.
          </Typography>
        </Stack>

        <Card component="section" aria-labelledby="checkout-title">
          <CardContent sx={{ p: { xs: 2.5, sm: 3.5 }, "&:last-child": { pb: { xs: 2.5, sm: 3.5 } } }}>
            <Stack spacing={2.5}>
              <Box>
                <Typography id="checkout-title" component="h2" variant="h2">
                  Riepilogo
                </Typography>
                <Typography color="text.secondary" variant="body2" sx={{ mt: 0.5 }}>
                  Importo totale
                </Typography>
                <Typography component="p" sx={{ mt: 0.25, fontSize: "2.5rem", fontWeight: 750, letterSpacing: "-0.04em" }}>
                  € 42,50
                </Typography>
              </Box>

              <Divider />

              {status === "ready" && (
                <Stack spacing={1.5}>
                  <Typography color="text.secondary" variant="body2">
                    Seleziona “Conferma pagamento” per proseguire.
                  </Typography>
                  <Button fullWidth size="large" variant="contained" onClick={submitPayment}>
                    Conferma pagamento
                  </Button>
                </Stack>
              )}

              {status === "pending" && (
                <Stack role="status" aria-live="polite" direction="row" spacing={1.5} sx={{ py: 1, alignItems: "center" }}>
                  <CircularProgress size={22} aria-label="Pagamento in corso" />
                  <Typography fontWeight={650}>Pagamento in corso…</Typography>
                </Stack>
              )}

              {status === "approved" && (
                <Alert severity="success" role="status" aria-live="polite">
                  Pagamento approvato.
                  {scenarios[scenario].attempts && ` Autorizzazione completata dopo ${scenarios[scenario].attempts} tentativi.`}
                </Alert>
              )}

              {status === "exhausted" && (
                <Alert severity="error" role="alert">
                  Non è stato possibile completare il pagamento dopo tre tentativi. Puoi scegliere un metodo di pagamento alternativo.
                </Alert>
              )}
            </Stack>
          </CardContent>
        </Card>

        <Card component="aside" variant="outlined" sx={{ mt: 2, boxShadow: "none", bgcolor: "rgba(255,255,255,0.65)" }}>
          <CardContent sx={{ p: 2.5, "&:last-child": { pb: 2.5 } }}>
            <FormControl>
              <FormLabel id="scenario-label" sx={{ fontWeight: 700, color: "text.primary" }}>
                Controlli demo · non visibili al cliente
              </FormLabel>
              <RadioGroup aria-labelledby="scenario-label" name="scenario" value={scenario} onChange={selectScenario} sx={{ mt: 0.5 }}>
                {Object.entries(scenarios).map(([value, item]) => (
                  <FormControlLabel key={value} value={value} control={<Radio size="small" />} label={item.label} />
                ))}
              </RadioGroup>
              <Button onClick={resetDemo} sx={{ alignSelf: "flex-start", mt: 0.5, px: 0 }}>
                Reimposta schermata
              </Button>
            </FormControl>
          </CardContent>
        </Card>

        <Typography component="p" variant="caption" color="text.secondary" sx={{ display: "block", mt: 2, textAlign: "center" }}>
          Prototipo UI: importo e risultati provengono da fixture locali; non viene inviata alcuna richiesta.
        </Typography>
      </Box>
    </Box>
  );
}
